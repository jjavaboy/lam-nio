package lam.mq.consumer.startup;

import lam.mq.consumer.DistributedActiveMQConsumer;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年8月22日
* @version 1.0
*/
public class MQConsumerDistributionStartup {
	
	static DistributedActiveMQConsumer consumer = null;
	
	static{
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				if(consumer != null){
					consumer.close();
				}
			}
		});
	}
	
	public static void main(String[] args){
		consumer = new DistributedActiveMQConsumer();
		consumer.start();
	}

}
