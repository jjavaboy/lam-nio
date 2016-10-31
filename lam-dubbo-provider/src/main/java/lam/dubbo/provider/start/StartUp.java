package lam.dubbo.provider.start;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
* <p>
* main class
* </p>
* @author linanmiao
* @date 2016年10月30日
* @versio 1.0
*/
public class StartUp {

	public static void main(String[] args){
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"dubbo-lam-provider.xml"});
		context.start();
		
		try{
			System.in.read();
			context.stop();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
