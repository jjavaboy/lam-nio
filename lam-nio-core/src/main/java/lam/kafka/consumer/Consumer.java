package lam.kafka.consumer;

import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import lam.kafka.KafkaProperties;
import lam.log.Console;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2018年6月19日
* @version 1.0
*/
public class Consumer {
	
	public static void main(String[] args) {
		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProperties.KAFKA_SERVER_HOST + ":" + KafkaProperties.KAFKA_SERVER_PORT);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "DemoConsumer");
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, Boolean.TRUE.toString());
		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
		props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "5000");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		
		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
		try {
			consumer.subscribe(Collections.singletonList("my-topic"));
			ConsumerRecords<String, String> records = consumer.poll(3000L);
			for (ConsumerRecord<String, String> record : records) {
				Console.println("Received message:" + record.key() + ", " + record.value() + "at offset " + record.offset());
			}
			Console.print("consumer finish");
		} finally {
			consumer.close();
		}
	}
}
