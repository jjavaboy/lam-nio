package lam.ratelimit;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
* <p>
* 滑动窗口限流
* </p>
* @author linanmiao
* @date 2017年8月18日
* @version 1.0
*/
public class SmoothLimiter {
	
    private static final int DEFAULT_WINDOWS = 10;
	
	/**
	 * 周期内的限流数
	 */
	private final int limit;
	
	/**
	 * 滑动窗口周期时长（毫秒）
	 */
	private final long interval;
	
	/**
	 * 窗口数量
	 */
	private final int windows;
	
	private final long eachInterval;
	
	private AtomicLong startTime;
	
	private final AtomicInteger[] counts;
	
	public SmoothLimiter(int limit, long interval){
		this(limit, interval, DEFAULT_WINDOWS);
	}
	
	public SmoothLimiter(int limit, long interval, int windows){
		this.limit = limit;
		this.interval = interval;
		this.windows = windows;
		this.eachInterval = this.interval / this.windows;
		this.counts = new AtomicInteger[this.windows];
		this.startTime = new AtomicLong(System.currentTimeMillis());
		init();
	}
	
	private void init(){
		for(int i = 0; i < counts.length; i++)
			counts[i] = new AtomicInteger(0);
	}
	
	public boolean grant(){
		return false;
	}
	
	private AtomicInteger selectCounter(long time){
		long start = startTime.get();
		if(time < start)
			time = start;
		for(int i = 0; i < windows; i++){
			if((start + eachInterval * i) <= time && time < (start + eachInterval * (i + 1)))
				return counts[i];
		}
		long now = System.currentTimeMillis();
		//如果CAS失败，说明startTime的value已经被修改过了。
		startTime.compareAndSet(start, now);
		return selectCounter(now);
	}

}
