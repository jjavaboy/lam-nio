package lam.dubbo.consumer.start;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import lam.dubbo.api.DemoUserService;

/**
* <p>
* consumer main class
* </p>
* @author linanmiao
* @date 2016年10月30日
* @versio 1.0
*/
public class LamDubboConsumerStartUp {
	
	public static void main(String[] args){
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"dubbo-lam-consumer-single.xml"});
        context.start();
        
        try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			context.close();
		}
	}

}
