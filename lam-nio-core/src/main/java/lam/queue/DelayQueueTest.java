package lam.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2018年1月8日
* @version 1.0
*/
public class DelayQueueTest {
	
	public static void main(String[] args) {
		//BlockingQueue<Delayed> queue = new java.util.concurrent.DelayQueue<Delayed>();
		DelayQueue<Delayed> queue = new java.util.concurrent.DelayQueue<>();
		boolean rs = queue.add(new MyDelayed());//add()==>>offer()
		
		try {
			Delayed delayed = queue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private static class MyDelayed implements Delayed {

		@Override
		public int compareTo(Delayed o) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public long getDelay(TimeUnit unit) {
			// TODO Auto-generated method stub
			return 0;
		}
		
	}

}
