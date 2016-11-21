package lam.rocketmq.consumer;

import java.util.List;

import org.junit.Test;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.MessageExt;

public class MsgPushConsumer {
	
	public static void main(String[] args){
		new MsgPushConsumer().consume();
	}
	
	@Test
	public void consume(){
		String namesrvAddr = "192.168.204.127:9876";
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("MsgConsumer");
		consumer.setNamesrvAddr(namesrvAddr);
		
		try {
			consumer.subscribe("Topic", "MsgTag");
			consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
			consumer.registerMessageListener(new MessageListenerConcurrently(){
				@Override
				public ConsumeConcurrentlyStatus consumeMessage(
						List<MessageExt> list,	ConsumeConcurrentlyContext context) {
					System.out.println("size:" + list.size());
					MessageExt msgExt = list.get(0);
					System.out.println("msgExt:" + msgExt.toString());
					return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
				}
			});
			
			consumer.start();
			
		} catch (MQClientException e) {
			e.printStackTrace();
		}
	}

}
