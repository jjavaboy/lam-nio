package lam.kafka.streams;

import java.util.Arrays;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.apache.kafka.streams.state.KeyValueStore;

import lam.kafka.KafkaProperties;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2018年6月21日
* @version 1.0
*/
public class WordCount {
	
	public static void main(String[] args) {
		Properties props = new Properties();
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-wordcount");
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProperties.KAFKA_SERVER_HOST + ":" + KafkaProperties.KAFKA_SERVER_PORT);
		props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		
		final StreamsBuilder builder = new StreamsBuilder();
		KStream<String, String> source = builder.stream(KafkaProperties.STREAMS_TOPIC);
		
		KTable<String, Long> counts = source.flatMapValues(new ValueMapper<String, Iterable<String>>(){
			@Override
			public Iterable<String> apply(String value) {
				return Arrays.asList(value.toLowerCase(Locale.getDefault()).split("\\W+"));
			}
			})
			.groupBy(new KeyValueMapper<String, String, String>() {
				@Override
				public String apply(String key, String value) {
					return value;
				}
			})
		      // Materialize the result into a KeyValueStore named "counts-store".
		      // The Materialized store is always of type <Bytes, byte[]> as this is the format of the inner most store.
			.count(Materialized.<String, Long, KeyValueStore<Bytes, byte[]>> as("counts-store"));
		
		counts.toStream().to("streams-wordcount-output", Produced.with(Serdes.String(), Serdes.Long()));
		
		//words.to(KafkaProperties.STREAMS_OTHER_TOPIC);
		
		final Topology topology = builder.build();
		
		System.out.println(topology.describe());
		
		final KafkaStreams streams = new KafkaStreams(topology, props);
		final CountDownLatch latch = new CountDownLatch(1);
		
		Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
			@Override
			public void run() {
				streams.close();
				latch.countDown();
			}
		});
		
		try {
			streams.start();
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.exit(0);
	}

}
