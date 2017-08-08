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
	
	private LoginService loginService;
	
	private DemoUserService userService;
	
	private static ThreadPoolExecutor executor = new ThreadPoolExecutor(
			3, 3, 1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), 
		    Executors.defaultThreadFactory(), 
		    new ThreadPoolExecutor.AbortPolicy());
	
	public void start(){
		executor.execute(new StartRunnable());
	}
	
	private void sleepMilli(long timeout){
		try {
			TimeUnit.MILLISECONDS.sleep(timeout);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
	}
	
	public void setUserService(DemoUserService userService) {
		this.userService = userService;
	}
	
	private class StartRunnable implements Runnable{
		@Override
		public void run() {
			try{
				for(int i = 0; i < 5; i++){
					String username = "user_" + i; 
				    boolean li = loginService.login(username);
			    	/*String hello = userService.sayHello(username);
			    	String goodBye = userService.sayGoodBye(username);
			    	boolean lo = loginService.logout(username);
			    	logger.info(String.format("login:%b, %s, %s, logout:%b", li, hello, goodBye, lo));*/
				    logger.info(String.format("login:%b", li));
			    	sleepMilli(1L);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

}
