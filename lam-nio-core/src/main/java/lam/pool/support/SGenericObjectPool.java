package lam.pool.support;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
* <p>
* object pool class
* </p>
* @author linanmiao
* @date 2017年3月24日
* @version 1.0
*/
public class SGenericObjectPool<T> extends SBaseGenericObjectPool<T>{

	private final java.util.concurrent.LinkedBlockingDeque<SPooledObject<T>> idleObjects;
	private final Map<SObjectWrapper<T>, SPooledObject<T>>
		allObjects = new ConcurrentHashMap<SObjectWrapper<T>, SPooledObject<T>>();
	
	public SGenericObjectPool(Builder<T> builder){
		super(builder);
		idleObjects = new java.util.concurrent.LinkedBlockingDeque<SPooledObject<T>>();
		startEvictor(super.timeBetweenEvictorRunsMillis);
	}
	
	public T borrowObject()throws SPoolException{
		boolean create = false;
		SPooledObject<T> p = null;
		while(p == null){
			if(blockWhenExhausted){
				p = idleObjects.pollFirst();
				if(p == null){
					p = create();
				}
			}else{
				
			}
		}
		return p.getObject();
	}
	
	private SPooledObject<T> create() {
		if(createCount.get() >= maxTotal || createCount.get() >= Integer.MAX_VALUE){
			return null;
		}
		return null;
	}

	/**
	 * abandon the excess idle object
	 */
	public void startEvictor(long timeBetweenEvictorRunsMillis){
		
	}
	
	public static class Builder<T>{
		
		//======================attribute with default value==================
		
		private int maxTotal = 8;
		private int maxIdle = 8;
		private long maxWaitMillis = -1;//wait forever
		private boolean blockWhenExhausted = Boolean.TRUE.booleanValue();
		private boolean testOnCreate = Boolean.FALSE.booleanValue();
		private boolean testOnBorrow = Boolean.FALSE.booleanValue();
		private boolean testOnReturn = Boolean.FALSE.booleanValue();
		private long timeBetweenEvictorRunsMillis = -1;
		
		private SPooledObjectFactory<T> factory;
		
		public Builder(){}
		
		public int getMaxTotal() {
			return maxTotal;
		}

		public Builder<T> setMaxTotal(int maxTotal) {
			this.maxTotal = maxTotal;
			return this;
		}

		public int getMaxIdle() {
			return maxIdle;
		}

		public Builder<T> setMaxIdle(int maxIdle) {
			this.maxIdle = maxIdle;
			return this;
		}
		
		public long getMaxWaitMillis() {
			return maxWaitMillis;
		}
		
		public Builder<T> setMaxWaitMillis(long maxWaitMillis) {
			this.maxWaitMillis = maxWaitMillis;
			return this;
		}
		
		public boolean isBlockWhenExhausted() {
			return blockWhenExhausted;
		}
		
		public Builder<T> setBlockWhenExhausted(boolean blockWhenExhausted) {
			this.blockWhenExhausted = blockWhenExhausted;
			return this;
		}

		public boolean isTestOnCreate() {
			return testOnCreate;
		}

		public Builder<T> setTestOnCreate(boolean testOnCreate) {
			this.testOnCreate = testOnCreate;
			return this;
		}

		public boolean isTestOnBorrow() {
			return testOnBorrow;
		}

		public Builder<T> setTestOnBorrow(boolean testOnBorrow) {
			this.testOnBorrow = testOnBorrow;
			return this;
		}

		public boolean isTestOnReturn() {
			return testOnReturn;
		}

		public Builder<T> setTestOnReturn(boolean testOnReturn) {
			this.testOnReturn = testOnReturn;
			return this;
		}
		
		public long getTimeBetweenEvictorRunsMillis() {
			return timeBetweenEvictorRunsMillis;
		}
		
		public Builder<T> setTimeBetweenEvictorRunsMillis(long timeBetweenEvictorRunsMillis) {
			this.timeBetweenEvictorRunsMillis = timeBetweenEvictorRunsMillis;
			return this;
		}
		
		public SPooledObjectFactory<T> getFactory() {
			return factory;
		}
		
		public Builder<T> setFactory(SPooledObjectFactory<T> factory) {
			this.factory = factory;
			return this;
		}

		public SGenericObjectPool<T> build(){
			return new SGenericObjectPool<T>(this);
		}
	}

}
