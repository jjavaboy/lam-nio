package lam.schedule.launch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
* <p>
* launch class
* </p>
* @author linanmiao
* @date 2017年5月8日
* @version 1.0
*/
public class LamScheduleLaunch {
	
	private static Logger logger = LoggerFactory.getLogger(LamScheduleLaunch.class);
	
	public static void main(String[] args){
		final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"spring-context.xml"});
		context.start();
		
		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run(){
				context.close();
				logger.info("shutdown hook runs.");
			}
		});
	}

}
