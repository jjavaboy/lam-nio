package lam.dubbo.provider.api.impl;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年11月10日
* @version 1.0
*/
public class MergeProvider2 {
	
	public static void main(String[] args) {
		String configLocation = MergeProvider2.class.getPackage().getName().replace(".", "/") + "/" + "merge-provider2.xml";
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(configLocation);
		context.start();
		
		synchronized (MergeProvider2.class) {
			try {
				MergeProvider2.class.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		context.close();
	}

}
