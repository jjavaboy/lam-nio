package lam.rocketmq.producer;

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
	
	public void produce(){
		String namesrvAddr = "192.168.204.127:9876";
		DefaultMQProducer producer = new DefaultMQProducer("Producer");
		producer.setNamesrvAddr(namesrvAddr);
		try {
			producer.start();
			
			for(int i = 0; i < 10; i++){
				Message msg = new Message("Topic", "MsgTag", "key" + i, "my message-我的消息".getBytes());
				SendResult result = producer.send(msg);
				System.out.println(String.format("tag:%s, key:%s, body:%s, rs:%s", msg.getTags(), msg.getKeys(), new String(msg.getBody()), result.getSendStatus().toString()));
			}
			
			for(int i = 0; i < 10; i++){
				Message msg = new Message("Topic", "MsgTag2", "key" + i, "my message-我的消息".getBytes());
				SendResult result = producer.send(msg);
				System.out.println(String.format("tag:%s, key:%s, body:%s, rs:%s", msg.getTags(), msg.getKeys(), new String(msg.getBody()), result.getSendStatus().toString()));
			}
			
		} catch (MQClientException e) {
			e.printStackTrace();
		} catch (RemotingException e) {
			e.printStackTrace();
		} catch (MQBrokerException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			producer.shutdown();
		}
	}

}
