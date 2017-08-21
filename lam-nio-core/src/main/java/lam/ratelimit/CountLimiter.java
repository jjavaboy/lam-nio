package lam.ratelimit;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
* <p>
* 计算器限流
* </p>
* @author linanmiao
* @date 2017年8月18日
* @version 1.0
*/
public class CountLimiter {
	
	/**
	 * 最大次数
	 */
	private final int limit;
	
	/**
	 * 时间窗口长度(时间单位:毫秒)
	 */
	private final long interval;
	
	/**
	 * 计数开始时间
	 */
	private volatile long startTime;
	
	/**
	 * 剩余的次数
	 */
	private final AtomicInteger count;
	
	private final Lock resetLock;
	
	/**
	 * @param limit 时间窗口内的最大流量数
	 * @param interval 时间窗口长度，毫秒
	 */
	public CountLimiter(int limit, long interval){
		if(limit <= 0 || interval <= 0)
			throw new IllegalArgumentException(String.format("limit(%d) or interval(%d) is less than 0.", limit, interval));
		this.startTime = System.currentTimeMillis();
		this.limit = limit;
		this.interval = interval;
		this.count = new AtomicInteger(this.limit);
		this.resetLock = new ReentrantLock(); 
	}
	
	/**
	 * 授权true：可以进入不限制；false：限制不能进入
	 */
	public boolean grant(){
		//到达新时间窗口，则计数复位
		if(System.currentTimeMillis() >= startTime + interval){
			resetLock.lock();
			try{
				long now = System.currentTimeMillis();
				if(now >= startTime + interval){
					startTime = now;
					count.set(limit);
				}
			}finally{
				resetLock.unlock();
			}
		}
		if(count.getAndDecrement() < 1)
			return false;
		return true;
	}

}
