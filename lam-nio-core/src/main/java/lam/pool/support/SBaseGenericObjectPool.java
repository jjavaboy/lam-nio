package lam.pool.support;

import java.util.concurrent.atomic.AtomicLong;

import lam.pool.support.SGenericObjectPool.Builder;

/**
* <p>
* abstract class of GenericObjectPool
* Hold the attribute of pool
* </p>
* @author linanmiao
* @date 2017年3月24日
* @version 1.0
*/
public abstract class SBaseGenericObjectPool<T>{
	
	//===============attribute, can be set================
	
	protected volatile int maxTotal;
	protected volatile int maxIdle;
	protected volatile int minIdle;
	protected volatile long maxWaitMillis;
	protected volatile boolean blockWhenExhausted;
	protected volatile boolean testOnCreate;
	protected volatile boolean testOnBorrow;
	protected volatile boolean testOnReturn;
	protected volatile long timeBetweenEvictorRunsMillis;
	protected volatile int numTestsPerEvictionRun;
	protected volatile boolean lifo;
	protected volatile boolean testWhileIdle;
	protected volatile long minEvictableIdleTimeMillis;// = 30 * 60 * 1000;//half of an hour
	
	protected final SPooledObjectFactory<T> factory;
	
	/**
	 * To mark whether the pool is closed. 
	 */
	protected volatile boolean close;
	protected final Object closeLock = new Object();
	
	/**
	 * create object count, which is not destroy.
	 */
	protected final AtomicLong createCount = new AtomicLong(0L);
	/**
	 * created object count successful in history
	 */
	protected final AtomicLong createdCount = new AtomicLong(0L);
	protected final AtomicLong destroyedCount = new AtomicLong(0L);
	
	public SBaseGenericObjectPool(SGenericObjectPool.Builder<T> builder){
		setMaxTotal(builder.getMaxTotal());
		setMaxIdle(builder.getMaxIdle());
		setMinIdle(builder.getMinIdle());
		setMaxWaitMillis(builder.getMaxWaitMillis());
		setBlockWhenExhausted(builder.isBlockWhenExhausted());
		setTestOnCreate(builder.isTestOnCreate());
		setTestOnBorrow(builder.isTestOnBorrow());
		setTestOnReturn(builder.isTestOnReturn());
		setTimeBetweenEvictorRunsMillis(builder.getTimeBetweenEvictorRunsMillis());
		setNumTestsPerEvictionRun(builder.getNumTestsPerEvictionRun());
		setTestWhileIdle(builder.isTestWhileIdle());
		setMinEvictableIdleTimeMillis(builder.getMinEvictableIdleTimeMillis());
		setLifo(builder.isLifo());
		
		this.factory = builder.getFactory();
	}

	public void setMaxTotal(int maxTotal) {
		checkPositive(maxTotal);
		this.maxTotal = maxTotal;
	}

	public void setMaxIdle(int maxIdle) {
		checkNotNegative(maxIdle);
		this.maxIdle = maxIdle;
	}
	
	public void setMinIdle(int minIdle) {
		checkNotNegative(minIdle);
		this.minIdle = minIdle;
	}

	public void setMaxWaitMillis(long maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}

	public void setBlockWhenExhausted(boolean blockWhenExhausted) {
		this.blockWhenExhausted = blockWhenExhausted;
	}

	public void setTestOnCreate(boolean testOnCreate) {
		this.testOnCreate = testOnCreate;
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public void setTestOnReturn(boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
	}

	public void setTimeBetweenEvictorRunsMillis(long timeBetweenEvictorRunsMillis) {
		this.timeBetweenEvictorRunsMillis = timeBetweenEvictorRunsMillis;
	}
	
	public void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
		checkNotNegative(numTestsPerEvictionRun);
		this.numTestsPerEvictionRun = numTestsPerEvictionRun;
	}
	
	public void setTestWhileIdle(boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
	}
	
	public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
		checkNotNegative(numTestsPerEvictionRun);
		this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
	}
	
	public void setLifo(boolean lifo) {
		this.lifo = lifo;
	}
	
	public void checkPositive(int i){
		if(i <= 0){
			throw new IllegalArgumentException("argument " + i + " <= 0, it must be positive.");
		}
	}
	
	public void checkNotNegative(int i){
		if(i < 0){
			throw new IllegalArgumentException("argument " + i + " < 0, it must be not negative.");
		}
	}

}
