package lam.kafka.producer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import lam.kafka.KafkaProperties;
import lam.log.Console;
import lam.util.Threads;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2018年6月19日
* @version 1.0
*/
public class Producer {
	
	public static void main(String[] args) {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProperties.KAFKA_SERVER_HOST + ":" + KafkaProperties.KAFKA_SERVER_PORT);
		props.put(ProducerConfig.CLIENT_ID_CONFIG, "DemoProducer");
		props.put(ProducerConfig.ACKS_CONFIG, "all");
		props.put(ProducerConfig.RETRIES_CONFIG, "0");
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, "16384");
		props.put(ProducerConfig.LINGER_MS_CONFIG, "1");
		props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, "33554432");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		
		KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);
		try {
		int messageNo = 2;
			try {
				//Future<RecordMetadata> future = 
				producer.send(new ProducerRecord<String, String>(KafkaProperties.MY_TOPIC, String.valueOf(messageNo), "Message_" + messageNo));
				//RecordMetadata metadata = future.get();
				
				//Console.print(metadata);
				
				Console.println("producer finish.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} finally {
			producer.close();
		}
	}

}
