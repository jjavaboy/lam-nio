package lam.transaction.tcc.test;

import java.io.IOException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import lam.transaction.tcc.exception.CanceledException;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年11月29日
* @version 1.0
*/
public class TccTransactionTest {
	
	public static void main(String[] args) {
		String configLocation = TccTransactionTest.class.getPackage().getName().replace(".", "/") + "/" + "tcc-transaction.xml";
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(configLocation);
		context.start();
		
		MyService myService = context.getBean("myService", MyService.class);
		try {
			String s = myService.doIt0("abc", 2, (byte)1, true);
			System.out.println(s);
		} catch (CanceledException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		context.close();
	}

}
