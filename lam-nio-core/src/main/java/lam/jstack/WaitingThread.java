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
* @date 2018年5月10日
* @versio 1.0
*/
public class WaitingThread {
	
	static ThreadPoolExecutor executor;
	
	static {
		executor = new ThreadPoolExecutor(3, 3, Long.MAX_VALUE, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(),
				Executors.defaultThreadFactory(),
				new ThreadPoolExecutor.AbortPolicy());
	}
	
	public static void main(String[] args) {
		executor.execute(new InnerThread());
		executor.execute(new InnerThread());
	}
	
	static class InnerThread implements Runnable {
		
		private static Object lock = new Object();

		@Override
		public void run() {
			synchronized (lock) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
		
	}

}
