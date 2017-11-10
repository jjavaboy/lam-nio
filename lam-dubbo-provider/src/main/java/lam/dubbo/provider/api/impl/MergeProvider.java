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
public class MergeProvider {
	
	public static void main(String[] args) {
		String configLocation = MergeProvider.class.getPackage().getName().replace(".", "/") + "/" + "merge-provider.xml";
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(configLocation);
		context.start();
		synchronized (MergeProvider.class) {
			try {
				MergeProvider.class.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		context.close();
	}

}
