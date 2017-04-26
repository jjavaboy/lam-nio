package lam.dubbo.consumer.controller;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lam.dubbo.api.LoginService;
import lam.dubbo.api.UserService;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年3月20日
* @version 1.0
*/
public class LamController {
	
	private LoginService loginService;
	
	private UserService userService;
	
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
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	private class StartRunnable implements Runnable{
		@Override
		public void run() {
			try{
				for(int i = 0; i < 100; i++){
					String username = "user_" + i; 
				    boolean li = loginService.login(username);
			    	String hello = userService.sayHello(username);
			    	String goodBye = userService.sayGoodBye(username);
			    	boolean lo = loginService.logout(username);
			    	System.out.println(String.format("login:%b, %s, %s, logout:%b", li, hello, goodBye, lo));
			    	sleepMilli(800L);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

}
