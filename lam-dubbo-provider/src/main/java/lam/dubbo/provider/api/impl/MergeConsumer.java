package lam.dubbo.provider.api.impl;

import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import lam.dubbo.api.MergeService;
import lam.log.Console;
import lam.util.Threads;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年11月10日
* @version 1.0
*/
public class MergeConsumer {
	
	public static void main(String[] args) {
		String configLocation = MergeConsumer.class.getPackage().getName().replace(".", "/") + "/" + "merge-consumer.xml";
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(configLocation);
		context.start();
		
		MergeService mergeService = context.getBean("mergeService", MergeService.class);
		for (int i = 0; i < 10; i++) {
			List<String> list = mergeService.mergerResult();
			Console.println("mergeResult(): " + list);
			Threads.sleepWithUninterrupt(1000L);
		}
		context.close();
	}

}
