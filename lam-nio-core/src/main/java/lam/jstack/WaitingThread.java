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
		Object lock = new Object();
		executor.execute(new InnerWaitThread(lock));
		executor.execute(new InnerNotifyThread(lock));
	}
	
	static class InnerWaitThread implements Runnable {
		
		private Object lock;

		InnerWaitThread(Object lock) {
			this.lock = lock;
		}
		
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
	
	static class InnerNotifyThread implements Runnable {

		private Object lock;
		
		InnerNotifyThread(Object lock) {
			this.lock = lock;
		}
		
		@Override
		public void run() {
			synchronized (lock) {
					try {
						Thread.sleep(Integer.MAX_VALUE);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						try {						
							lock.notifyAll();
						} catch (IllegalMonitorStateException e) {
							e.printStackTrace();
						}
					}
			}
		}
		
	}

}
