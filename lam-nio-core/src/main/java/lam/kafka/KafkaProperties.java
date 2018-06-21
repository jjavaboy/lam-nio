package lam.kafka;
/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2018年6月19日
* @version 1.0
*/
public class KafkaProperties {
	
	public static final String KAFKA_SERVER_HOST = "192.168.204.79";
	
	public static final int KAFKA_SERVER_PORT = 9092;
	
	public static final String ZOOKEEPER_CONNECT = "129.168.204.79:2181";
	
	public static final String MY_TOPIC = "my-topic";
	
	public static final String STREAMS_TOPIC = "streams-plaintext-input"; //"streams-plaintext-input"
	
	public static final String STREAMS_OTHER_TOPIC = "streams-plaintext-output";
	
	public static final long TIME_OUT_MILLISECOND = 3000L;
	
	private KafkaProperties() {}

}
