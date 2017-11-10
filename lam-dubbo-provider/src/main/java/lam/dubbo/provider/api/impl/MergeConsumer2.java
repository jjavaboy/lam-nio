package lam.dubbo.provider.api.impl;

import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import lam.dubbo.api.MergeService;
import lam.log.Console;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年11月10日
* @version 1.0
*/
public class MergeConsumer2 {
	
	public static void main(String[] args) {
		String configLocation = MergeConsumer2.class.getPackage().getName().replace(".", "/") + "/" + "merge-consumer2.xml";
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(configLocation);
		context.start();
		
		MergeService mergeService = context.getBean("mergeService", MergeService.class);
		for (int i = 0; i < 5; i++) {
			List<Integer> ints = mergeService.mergerResult2();
			Console.println("mergerResult2:" + ints);

			List<String> list = mergeService.mergerResult();
			Console.println("mergerResult:" + list);
			
		}
		
		context.close();
	}

}
