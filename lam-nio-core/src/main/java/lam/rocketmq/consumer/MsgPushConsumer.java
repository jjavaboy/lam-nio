package lam.rocketmq.consumer;

import java.util.List;

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
	
	public void consume(){
		String namesrvAddr = "192.168.204.127:9876";
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("MsgConsumer");
		consumer.setNamesrvAddr(namesrvAddr);
		
		try {
			//can subscribe many tags , format like 'MsgTag || MsgTag2 ...'
			consumer.subscribe("Topic", "MsgTag || MsgTag2");
			consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
			consumer.registerMessageListener(new MessageListenerConcurrently(){
				@Override
				public ConsumeConcurrentlyStatus consumeMessage(
						List<MessageExt> list,	ConsumeConcurrentlyContext context) {
					MessageExt msgExt = list.get(0);
					System.out.println(String.format("%s, %s, %s", msgExt.getTags(), msgExt.getTopic(), new String(msgExt.getBody())));
					
					//ConsumeConcurrentlyStatus.RECONSUME_LATER;
					return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
				}
			});
			
			consumer.start();
			
		} catch (MQClientException e) {
			e.printStackTrace();
		}
	}

}