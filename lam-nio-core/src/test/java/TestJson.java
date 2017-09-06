import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.RateLimiter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import lam.log.Console;
import lam.netty.http.json.factory.OrderFactory;
import lam.netty.http.json.model.Order;
import lam.pool.thread.LRejectedExecutionHandler;
import lam.pool.thread.LThreadPoolExecutor;
import lam.util.concurrent.ThreadFactoryBuilder;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2016年10月9日
* @versio 1.0
*/
public class TestJson implements Cloneable{
	
	private int id;
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	
	/**
	 * public interface Future<V> {
	 * 
	 * boolean cancel(boolean mayInterruptIfRunning);
	 * 
	 * boolean isCancelled();
	 * 
	 * boolean isDone();
	 * 
	 * V get() throws InterruptedException, ExecutionException;
	 * 
	 * V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException;
	 * 
	 * }
	 */
	
	public static void main(String[] args) {
		RateLimiter rateLimiter = RateLimiter.create(5);
		//sleepWithUninterrupt(2000L);
		System.out.println(rateLimiter.acquire(10));
		sleepWithUninterrupt(2000L);
		System.out.println(rateLimiter.acquire(10));
	}
	
	private static void sleepWithUninterrupt(long millisecond){
		boolean isInterrupted = false;
		long remainSleepTime = millisecond;
		long start = System.currentTimeMillis();
		try{
			while(true){
				try {
					//If [millisecond] less than or equal to zero, do not sleep at all.
					TimeUnit.MILLISECONDS.sleep(remainSleepTime); //do not use : Thread.sleep(millisecond);
					return ;
				} catch (InterruptedException e) {
					e.printStackTrace();
					isInterrupted = true;
					remainSleepTime = System.currentTimeMillis() - start;
				}
			}
		}finally{
			if(isInterrupted){
				Thread.currentThread().interrupt();
			}
		}
	}
	
	static ThreadLocal<String> threadName = new ThreadLocal<String>();
	
	enum SingleEnum{
		INSTANCE;
		
		private Single single;
		
		private SingleEnum(){
			single = new Single();
		}
		
		public Single getSigngle(){
			return INSTANCE.single;
		}
		
		class Single{
			private Single(){}
			
			public void doSomething(){
				System.out.println("我是谁-hashCode:" + System.identityHashCode(this));			
			}
		}
	}
	

}
