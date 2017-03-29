package lam.pool.support;

import java.io.PrintWriter;
import java.util.Deque;

import lam.queue.LDeque;

/**
* <p>
* default pooled object
* </p>
* @author linanmiao
* @date 2017年3月23日
* @versio 1.0
*/
public class SDefaultPooledObject<T> /*extends SPooledObject<T>*/ extends SPooledObject<T>{
	
	private final T object;
	private SPooledObjectState state = SPooledObjectState.IDLE;
	private final long createTime = System.currentTimeMillis();
	private volatile long lastBorrowTime = createTime;
	private volatile long lastUseTime = createTime;
	private volatile long lastReturnTime = createTime;
	private volatile boolean lobAbandoned = Boolean.FALSE.booleanValue();
	private volatile Exception borrowedBy = null;
	private volatile Exception useBy = null;
	private volatile int borrowedCount = 0;
	
	public SDefaultPooledObject(T t){
		this.object = t;
	}

	@Override
	public T getSObject() {
		return this.object;
	}

	@Override
	public long getSCreateTime() {
		return this.createTime;
	}

	@Override
	public long getSActiveTimeMillis() {
		long lReturnTime = this.lastReturnTime;
		long lBorrowTime = this.lastBorrowTime;
		if(lReturnTime > lBorrowTime){
			return lReturnTime - lBorrowTime;
		}else{
			return System.currentTimeMillis() - lBorrowTime;
		}
	}

	@Override
	public long getSIdleTimeMillis() {
		long now = System.currentTimeMillis();
		//lastReturnTime maybe update by other thread, so make it final.
		final long lReturnTime = this.lastReturnTime;
		if(now > lReturnTime){
			return now - lReturnTime;
		}else{
			return 0;
		}
	}

	@Override
	public long getSLastBorrowTime() {
		return this.lastBorrowTime;
	}

	@Override
	public long getSLastReturnTime() {
		return this.lastReturnTime;
	}

	@Override
	public long getSLastUsedTime() {
		return this.lastUseTime;
	}

	@Override
	public synchronized boolean startSEvictionTest() {
		if(this.state == SPooledObjectState.IDLE){
			this.state = SPooledObjectState.EVICTION;
			return true;
		}
		return false;
	}

	@Override
	public synchronized boolean endSEvictionTest(LDeque<SPooledObject<T>> idleQueue) {
		if(this.state == SPooledObjectState.EVICTION){
			this.state = SPooledObjectState.IDLE;
			return true;
		}else if(this.state == SPooledObjectState.EVICTION_RETURN_TO_HEAD){
			this.state = SPooledObjectState.IDLE;
			idleQueue.addFirst(this);
		}
		return false;
	}

	@Override
	public synchronized boolean allocate() {
		if(this.state == SPooledObjectState.IDLE){
			this.lastBorrowTime = System.currentTimeMillis();
			this.lastUseTime = this.lastBorrowTime;
			this.borrowedCount++;
			this.state = SPooledObjectState.ALLOCATED;
			return true;
		}else if(this.state == SPooledObjectState.EVICTION){
			this.state = SPooledObjectState.EVICTION_RETURN_TO_HEAD;
			return false;
		}
		return false;
	}

	@Override
	public synchronized boolean deallocate() {
		if(this.state == SPooledObjectState.ALLOCATED || this.state == SPooledObjectState.RETURNING){
			this.lastReturnTime = System.currentTimeMillis();
			this.state = SPooledObjectState.IDLE;
			return true;
		}
		return false;
	}

	@Override
	public synchronized void invalidate() {
		this.state = SPooledObjectState.INVALID;
	}

	@Override
	public void setLogAbandoned(boolean logAbandoned) {
		// TODO Auto-generated method stub
	}

	@Override
	public void use() {
		this.lastUseTime = System.currentTimeMillis();
	}

	@Override
	public void printStackTrace(PrintWriter writer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void markAbandoned() {
		this.state = SPooledObjectState.ABANDONED;
	}

	@Override
	public void markReturning() {
		this.state = SPooledObjectState.RETURNING;
	}
	
	public int compareTo(SPooledObject<T> other) {
		final long lastReturnTimeDiff = this.lastReturnTime - other.getSLastReturnTime();
		if(lastReturnTimeDiff == 0){
			return System.identityHashCode(this) - System.identityHashCode(other);
		}
		return (int) Math.min(Math.max(lastReturnTimeDiff, Integer.MIN_VALUE), Integer.MAX_VALUE);
	}

	@Override
	public SPooledObjectState getSState() {
		return state;
	}

}
