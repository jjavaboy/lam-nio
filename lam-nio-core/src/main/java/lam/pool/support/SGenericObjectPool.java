package lam.pool.support;

import java.io.Closeable;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import lam.queue.blocking.LBlockingDeque;

/**
* <p>
* object pool class
* </p>
* @author linanmiao
* @date 2017年3月24日
* @version 1.0
*/
public class SGenericObjectPool<T> extends SBaseGenericObjectPool<T> implements Closeable{

	//private final java.util.concurrent.LinkedBlockingDeque<SPooledObject<T>> idleObjects;
	private final lam.queue.blocking.impl.LLinkedBlockingDeque<SPooledObject<T>> idleObjects;
	private final Map<SObjectWrapper<T>, SPooledObject<T>>
		allObjects = new ConcurrentHashMap<SObjectWrapper<T>, SPooledObject<T>>();
	
	/**
	 * guarded by evictorLock
	 */
	private ScheduledThreadPoolExecutor evictorScheduledThreadPoolExecutor;
	
	/**
	 * guarding the evictor schudle
	 */
	protected final Object evictorLock = new Object();
	
	public SGenericObjectPool(Builder<T> builder){
		super(builder);
		//idleObjects = new java.util.concurrent.LinkedBlockingDeque<SPooledObject<T>>();
		idleObjects = new lam.queue.blocking.impl.LLinkedBlockingDeque<>();
		startEvictor(super.timeBetweenEvictorRunsMillis);
	}
	
	public T borrowObject()throws Exception{
		boolean create = false;
		SPooledObject<T> p = null;
		while(p == null){
			if(blockWhenExhausted){
				p = idleObjects.pollFirst();
				if(p == null && (p = create()) != null){
					create = true;
				}
				if(p == null){
					if(maxWaitMillis > -1){
						p = idleObjects.takeFirst();
					}else{
						p = idleObjects.pollFirst(maxWaitMillis, TimeUnit.MILLISECONDS);
					}
				}
				if(p == null){
					throw new NoSuchElementException("Time out(" + maxWaitMillis + ") waiting for idle object");
				}
				//to determine this object is idle
				if(!p.allocate()){
					//Means this object isn't idle, so get the next idel object
					p = null;
				}
			}else{
				p = idleObjects.pollFirst();
				if(p == null && (p = create()) != null){
					create = true;
				}
				if(p == null){
					throw new NoSuchElementException("Pool exhausted");
				}
				if(!p.allocate()){
					p = null;
				}
			}
			if(p != null){
				try{
					factory.activateSObject(p);
				}catch(Exception e){
					destroy(p);
					p = null;//Can let it to executes the next loop
					if(create){
						NoSuchElementException noSuchException = new NoSuchElementException("PooledObjectFactory factory activate object fail");
						noSuchException.initCause(e);
						throw noSuchException;
					}
				}
				if(p != null && (testOnBorrow || (create && testOnCreate))){
					boolean validate = false;
					Exception e1 = null;
					try{
						validate = factory.validateSObject(p);
					}catch(Exception e){
						e1 = e;
					}
					if(!validate){
						destroy(p);
						p = null;
						if(create){
							NoSuchElementException noSuchException = new NoSuchElementException("PooledObjectFactory factory activate object fail");
							noSuchException.initCause(e1);
							throw noSuchException;
						}
					}
				}
			}
		}
		return p.getSObject();
	}
	
	public void returnObject(T obj){
		SPooledObject<T> p = allObjects.get(new SObjectWrapper<T>(obj));
		if(p == null){
			return ;
		}
		synchronized (p) {
			final SPooledObjectState state = p.getSState();
			if(state != SPooledObjectState.ALLOCATED){
				throw new IllegalStateException("Object has been returned to pool or invalid");
			}else{
				p.markReturning();
			}
		}
		if(testOnReturn){
			boolean validate = factory.validateSObject(p);
			if(!validate){
				try {
					destroy(p);
				} catch (Exception e1) {
					return ;
				}
			}
		}
		try {
			factory.passivateSObject(p);
		} catch (Exception e) {
			try {
				destroy(p);
			} catch (Exception e1) {
				return ;
			}
		}
		if(!p.deallocate()){
			throw new IllegalStateException("Object has been returned to pool or invalid");
		}
		if(maxIdle > idleObjects.size()){
			try {
				destroy(p);
			} catch (Exception e) {
				return ;
			}
		}else{
			if(lifo){
				idleObjects.addFirst(p);
			}else{
				idleObjects.addLast(p);
			}
		}
	}
	
	public void invalidateObject(T obj) throws Exception{
		SPooledObject<T> p = allObjects.get(new SObjectWrapper<T>(obj));
		if(p == null){
			throw new IllegalStateException("object don't exists in this pool");
		}
		synchronized (p) {
			if(p.getSState() != SPooledObjectState.INVALID){
				destroy(p);
			}
		}
	}
	
	private void destroy(SPooledObject<T> p) throws Exception {
		p.invalidate();
		idleObjects.remove(p);
		allObjects.remove(new SObjectWrapper<T>(p.getSObject()));
		try{
			factory.destroySObject(p);
		}finally{
			createCount.decrementAndGet();
			destroyedCount.incrementAndGet();
		}
	}

	/**
	 * @return null if object number in the pool reaches the maxTotal
	 * @throws SPoolException
	 */
	private SPooledObject<T> create() throws SPoolException {
		long afterCreatedCount = createCount.incrementAndGet();
		if(afterCreatedCount > maxTotal || afterCreatedCount > Integer.MAX_VALUE){
			createCount.decrementAndGet();
			return null;
		}
		final SPooledObject<T> p;
		try {
			p = factory.makeSObject();
		} catch (Exception e) {
			createCount.decrementAndGet();
			throw new SPoolException("PooledObjectfactory make new object fail", e);
		}
		createdCount.incrementAndGet();
		allObjects.put(new SObjectWrapper<T>(p.getSObject()), p);
		return p;
	}

	/**
	 * abandon the excess idle object
	 */
	public void startEvictor(long timeBetweenEvictorRunsMillis){
		synchronized (evictorLock) {
			//stop the running evictor
			if(evictorScheduledThreadPoolExecutor != null){
				evictorScheduledThreadPoolExecutor.shutdown();
				evictorScheduledThreadPoolExecutor = null;
			}
			
			//then start the new evictor
			if(timeBetweenEvictorRunsMillis > 0){
				initEvictorSchedule();
				evictorScheduledThreadPoolExecutor.scheduleWithFixedDelay(
						new EvictionRunnable(), 
						timeBetweenEvictorRunsMillis, 
						timeBetweenEvictorRunsMillis, 
						TimeUnit.MILLISECONDS);
			}
		}
	}
	
	int getNumTestsPerEvictionRun(){
		int numTest = numTestsPerEvictionRun;
		return Math.min(numTest, idleObjects.size());
	}
	
	public void close(){
		if(isClosed()){
			return ;
		}
		synchronized (closeLock) {
			//double check
			if(isClosed()){
				return ;
			}
			super.close = Boolean.TRUE.booleanValue();
			
			//Just stop the evictor
			startEvictor(-1L);
			
			clear();
			
			//release the threads waiting for the idle object 
			//do here...
		}
	}
	
	public boolean isClosed(){
		return super.close;
	}
	
	public void clear(){
		SPooledObject<T> p = idleObjects.pollFirst();
		while(p != null){
			try {
				destroy(p);
			} catch (Exception e) {
			}
			p = idleObjects.pollFirst();
		}
	}
	
	private void ensureIdle(int idleCount, boolean always) throws Exception{
		if(idleCount < 1 || isClosed() || (!always && idleObjects.hasTakeWaiters())){
			return ;
		}
		while(idleObjects.size() < idleCount){
			SPooledObject<T> p = create();
			if(p == null){
				break;
			}
			if(lifo){
				idleObjects.addFirst(p);
			}else{
				idleObjects.addLast(p);
			}
		}
	}
	
	public int getNumIdle(){
		return this.idleObjects.size();
	}
	
	public int getNumActive(){
		return this.allObjects.size() - this.idleObjects.size();
	}
	
	/**
	 * get thread number waiting for the idle object
	 */
	public int getNumWaiters(){
		//handle here...
		return -1;
	}
	
	public void addObject() throws Exception{
		if(factory == null){
			throw new IllegalStateException("SPooledObjectFactory instance:factory can't be null");
		}
		SPooledObject<T> p = create();
		addIdleObject(p);
	}
	
	public void addIdleObject(SPooledObject<T> p) throws Exception{
		if(p != null){
			factory.passivateSObject(p);
			if(lifo){
				idleObjects.addFirst(p);
			}else{
				idleObjects.addLast(p);
			}
		}
	}
	
	private void initEvictorSchedule(){
		if(evictorScheduledThreadPoolExecutor == null){
			evictorScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(
					1, new EvictionThreadFactory(), new EvictionRejectedExecutionHandler());
		}else{
			throw new IllegalStateException("evictorScheduledThreadPoolExecutor is not null, no need to initialize.");
		}
	}
	
	private void evict() throws Exception{
		if(!idleObjects.isEmpty()){
			synchronized (evictorLock) {
				int numTest = getNumTestsPerEvictionRun();
				SEvictionPolicy<SPooledObject<T>> policy = new SDefaultEvictionPolicy();
				EvictionIterator<SPooledObject<T>> evictionIter = new EvictionIterator<SPooledObject<T>>(idleObjects);
				for(int i = 0; i < numTest; i++){
					if(!evictionIter.hasNext()){
						continue ;
					}
					SPooledObject<T> p = null;
					try{
						p = evictionIter.next();
					}catch(Exception e){
						continue;
					}
					
					if(!p.startSEvictionTest()){
						continue;
					}
					
					try{
						boolean evictRs = policy.evict(p);
						if(evictRs){
							destroy(p);
							continue;
						}
					}catch(Exception e){
					}
					if(testWhileIdle){
						try{
							factory.activateSObject(p);
							if(!factory.validateSObject(p)){
								destroy(p);
							}else{
								factory.passivateSObject(p);
							}
						}catch(Exception e){
							try{
								destroy(p);
							}catch(Exception e1){}
						}
					}
					if(!p.endSEvictionTest(idleObjects)){
						//do something in the furture.
					}
				}
			}
		}
	}
	
	//ThreadFactory===================
	public static class EvictionThreadFactory implements ThreadFactory{
		
		private static AtomicLong nFactorys = new AtomicLong(0L);
		private final AtomicLong nThreads = new AtomicLong(0L);
		private final ThreadGroup group;
		private final String namePrefix;
		
		public EvictionThreadFactory(){
			SecurityManager securityManager = System.getSecurityManager();
			group = securityManager != null ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
			namePrefix = String.format("%s-%d-thread-", getClass().getSimpleName(), nFactorys.incrementAndGet());
		}

		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(group, r, namePrefix + nThreads.incrementAndGet(), 0);
			if(t.isDaemon()){
				t.setDaemon(false);
			}
			if(t.getPriority() != Thread.NORM_PRIORITY){
				t.setPriority(Thread.NORM_PRIORITY);
			}
			return t;
		}
		
	}
	
	//RejectedExecutionHnadler===================
	public class EvictionRejectedExecutionHandler implements java.util.concurrent.RejectedExecutionHandler{
		
		public EvictionRejectedExecutionHandler(){}

		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			System.out.println("rejectedExecution Runnable:" + r);
		}
		
	}
	
	/**
	 * perform the task of eviction, to abandon the excess idle object.
	 */
	//EvictRunnable===================
	public class EvictionRunnable implements Runnable{

		public EvictionRunnable(){}
		
		@Override
		public void run() {
			//do task.
			try {
				evict();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			try {
				ensureIdle(minIdle, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	//EvictionIterator=================
	private class EvictionIterator<E> implements Iterator<E>{
		
		LBlockingDeque<E> deque;
		Iterator<E> iter;
		
		EvictionIterator(LBlockingDeque<E> deque){
			this.deque = deque;
			if(lifo){
				iter = deque.descendingIterator();
			}else{
				iter = deque.iterator();
			}
		}

		@Override
		public boolean hasNext() {
			return iter.hasNext();
		}

		@Override
		public E next() {
			return iter.next();
		}

		@Override
		public void remove() {
			iter.remove();
		}

		
	}
	
	//SDefaultEvictionPolicy=============
	private class SDefaultEvictionPolicy implements SEvictionPolicy<SPooledObject<T>>{

		@Override
		public boolean evict(SPooledObject<T> p) {
			if(minEvictableIdleTimeMillis < p.getSIdleTimeMillis() || minIdle < idleObjects.size()){
				return true;
			}
			return false;
		}
		
	}
	
	public static class Builder<T>{
		
		//======================attribute with default value==================
		
		private int maxTotal = 8;
		private int maxIdle = 8;
		private int minIdle = 0;
		private long maxWaitMillis = -1;//wait forever
		private boolean blockWhenExhausted = Boolean.TRUE.booleanValue();
		private boolean testOnCreate = Boolean.FALSE.booleanValue();
		private boolean testOnBorrow = Boolean.FALSE.booleanValue();
		private boolean testOnReturn = Boolean.FALSE.booleanValue();
		private long timeBetweenEvictorRunsMillis = -1;
		private int numTestsPerEvictionRun = 3;
		private boolean lifo = false;
		private boolean testWhileIdle = Boolean.FALSE.booleanValue();
		private long minEvictableIdleTimeMillis = 30 * 60 * 1000;//half of an hour
		
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

		public int getMinIdle() {
			return minIdle;
		}
		
		public Builder<T> setMinIdle(int minIdle) {
			this.minIdle = minIdle;
			return this;
		}
		
		public long getMaxWaitMillis() {
			return maxWaitMillis;
		}
		
		public Builder<T> setMaxWaitMillis(long maxWaitMillis) {
			this.maxWaitMillis = maxWaitMillis;
			return this;
		}
		
		public int getNumTestsPerEvictionRun() {
			return numTestsPerEvictionRun;
		}
		
		public Builder<T> setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
			this.numTestsPerEvictionRun = numTestsPerEvictionRun;
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
		
		public Builder<T> setLifo(boolean lifo) {
			this.lifo = lifo;
			return this;
		}
		
		public boolean isLifo() {
			return lifo;
		}
		
		public Builder<T> setTestWhileIdle(boolean testWhileIdle) {
			this.testWhileIdle = testWhileIdle;
			return this;
		}
		
		public boolean isTestWhileIdle() {
			return testWhileIdle;
		}
		
		public long getMinEvictableIdleTimeMillis() {
			return minEvictableIdleTimeMillis;
		}
		
		public Builder<T> setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
			this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
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
