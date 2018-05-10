package lam.jstack;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2018年5月10日
* @version 1.0
*/
public class LockThread {
	
	static Lock lock = new ReentrantLock();
	
	static ThreadPoolExecutor executor;
	
	static {
		executor = new ThreadPoolExecutor(2, 2, Long.MAX_VALUE, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(),
				Executors.defaultThreadFactory(),
				new ThreadPoolExecutor.AbortPolicy());
	}
	
	public static void main(String[] args) {
		executor.execute(new InnerThread());
		executor.execute(new InnerThread());
	}
	
	static class InnerThread implements Runnable {
		
		@Override
		public void run() {
			lock.lock();
			try {
				int i = 0;
				while (true) {
					i++;
				}
			} finally {
				lock.unlock();
			}
		}
		
	}

}
