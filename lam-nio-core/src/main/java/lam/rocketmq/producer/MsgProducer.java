package lam.rocketmq.producer;

import org.junit.Test;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.exception.RemotingException;

public class MsgProducer {
	
	public static void main(String[] args){
		new MsgProducer().produce();
	}
	
	@Test
	public void produce(){
		String namesrvAddr = "192.168.204.127:9876";
		DefaultMQProducer producer = new DefaultMQProducer("Producer");
		producer.setNamesrvAddr(namesrvAddr);
		try {
			producer.start();
			
//			printNameServerAddress(producer);
			
			Message msg = new Message("Topic", "MsgTag", "key1", "my message".getBytes());
			SendResult result = producer.send(msg);
			
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			producer.shutdown();
		}
	}
	
	public void printNameServerAddress(DefaultMQProducer producer){
		java.util.List<String> list =
		producer.getDefaultMQProducerImpl().getmQClientFactory().getMQClientAPIImpl().getNameServerAddressList();
		for(String str : list){
			System.out.println(str);
		}
	}

}
