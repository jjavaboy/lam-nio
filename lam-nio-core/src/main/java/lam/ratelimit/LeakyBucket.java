package lam.ratelimit;

import java.util.concurrent.atomic.AtomicInteger;

/**
* <p>
* 漏桶算法限流
* </p>
* @author linanmiao
* @date 2017年8月21日
* @version 1.0
*/
public class LeakyBucket {
	
	private volatile long timeStamp;
	/**
	 * 桶的最大容量
	 */
	private final int capacity;
	/**
	 * 每毫秒，水流出的速度
	 */
	private final int rate;
	/**
	 * 当前桶的水量
	 */
	private final AtomicInteger water;
	
	/**
	 * @param capacity 桶的最大容量
	 * @param rate 每毫秒，水流出的速度
	 */
	public LeakyBucket(int capacity, int rate){
		this.timeStamp = System.currentTimeMillis();
		this.capacity = capacity;
		this.rate = rate;
		this.water = new AtomicInteger(0);
	}
	
	public boolean grant(){
		long now = System.currentTimeMillis();
		int tempWater = water.get();
		while(!water.compareAndSet(tempWater, (int)Math.max(0, tempWater - (now - timeStamp) * rate)))
			tempWater = water.get();
		timeStamp = now;
		if(water.get() > capacity)
			return false;
		//如果加1后大于桶的容量，则水溢出。
		if(water.incrementAndGet() > capacity){
			//水溢出来
			water.decrementAndGet();
			return false;
		}
		return true;
	}
	
	public int getCurrentWater(){
		return water.get();
	}

}
