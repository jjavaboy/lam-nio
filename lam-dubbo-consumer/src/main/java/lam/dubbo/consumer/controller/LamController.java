package lam.dubbo.consumer.controller;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lam.dubbo.api.LoginService;
import lam.dubbo.api.DemoUserService;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年3月20日
* @version 1.0
*/
public class LamController {
	
	private static Logger logger = LoggerFactory.getLogger(LamController.class);
	
	private lam.dubbo.api.DemoUserService demoUserService;
	
	public void start(){
		new Thread(){
			public void run() {
				try {
					TimeUnit.SECONDS.sleep(30L);
				} catch (InterruptedException e2) {
					logger.error("sleep error", e2);
				}
		String result = demoUserService.sayHello("sky");
        
        logger.info("before disconnnect from zookeeper, result:" + result);
        
        logger.info("==========start to sleep in 30 seconds==============");
        try {
			TimeUnit.SECONDS.sleep(30L);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
        
        logger.info("==========finish to sleep in 30 seconds==============");
        
        for (int i = 0; i < 5; i++) {
	        result = demoUserService.sayHello("sky");
	        
	        logger.info("==>>after disconnnect from zookeeper. result:" + result);
	        
	        try {
				TimeUnit.SECONDS.sleep(1L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
			};
		}.start();
	}
	
	public void setDemoUserService(lam.dubbo.api.DemoUserService demoUserService) {
		this.demoUserService = demoUserService;
	}

}
