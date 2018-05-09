package lam.jstack;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2018年5月9日
* @versio 1.0
*/
public class CPU100Thread {
	
	static ThreadPoolExecutor executor;
	
	static {
		executor = new ThreadPoolExecutor(1, 1, Long.MAX_VALUE, TimeUnit.SECONDS, 
				new LinkedBlockingQueue<Runnable>(),
				Executors.defaultThreadFactory(),
				new ThreadPoolExecutor.AbortPolicy());
	}
	
	public static void main(String[] args) {
		executor.execute(new InnerThread());
	}
	
	
	static class InnerThread implements Runnable {

		@Override
		public void run() {
			long i = 0;
			while (true) {
				i++;
			}
		}
		
	}

}
