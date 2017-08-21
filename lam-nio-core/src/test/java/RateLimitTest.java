import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lam.ratelimit.CountLimiter;
import lam.ratelimit.LeakyBucket;
import lam.util.concurrent.ThreadFactoryBuilder;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年8月18日
* @version 1.0
*/
public class RateLimitTest {
	
	/**
	 * Google的guava提供限流工具类：RateLimiter，基于令牌桶的流控。
	 * https://github.com/google/guava/blob/v18.0/guava/src/com/google/common/util/concurrent/RateLimiter.java
	 */
	
	static Logger logger = LoggerFactory.getLogger(RateLimitTest.class);
	
	public static void main(String[] args){
		//countLimitTest();
		//leakyLimitTest();
	}
	
	/**
	 * 漏桶限流
	 */
	private static void leakyLimitTest(){
		final LeakyBucket leakyBucket = new LeakyBucket(500, 1);
		
		ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 10, 0L, TimeUnit.MILLISECONDS, 
				new LinkedBlockingQueue<Runnable>(),
				new ThreadFactoryBuilder().setThreadNamePrefix("RateLimitTest").build(),
				new ThreadPoolExecutor.AbortPolicy());
		for(int i = 0; i < executor.getCorePoolSize(); i++){
			executor.execute(new Runnable(){
				@Override
				public void run() {
					try {
						Thread.sleep(8);
					} catch (InterruptedException e) {
						logger.error("thread sleep error", e);
					}
				}});
		}
		
		for(int j = 0; j < 10000; j++){
			executor.execute(new Runnable(){
				@Override
				public void run() {
					if(leakyBucket.grant()){
						logger.info("grant success, current water:" + leakyBucket.getCurrentWater());
					}else{
						logger.warn("grant fail, current water:" + leakyBucket.getCurrentWater());
					}
				}
			});
		}
	}
	
	/**
	 * 计数限流
	 */
	private static void countLimitTest(){
		final CountLimiter countLimiter = new CountLimiter(500, 100);
		
		ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 10, 0L, TimeUnit.MILLISECONDS, 
				new LinkedBlockingQueue<Runnable>(),
				new ThreadFactoryBuilder().setThreadNamePrefix("RateLimitTest").build(),
				new ThreadPoolExecutor.AbortPolicy());
		for(int i = 0; i < executor.getCorePoolSize(); i++){
			executor.execute(new Runnable(){
				@Override
				public void run() {
					try {
						Thread.sleep(8);
					} catch (InterruptedException e) {
						logger.error("thread sleep error", e);
					}
				}});
		}
		
		for(int j = 0; j < 10000; j++){
			executor.execute(new Runnable(){
				@Override
				public void run() {
					if(countLimiter.grant()){
						logger.info("grant success");
					}else{
						logger.warn("grant fail");
					}
				}
			});
		}
	}

}
