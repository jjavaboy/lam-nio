package lam.pool.support;

import java.io.PrintWriter;
import java.util.Deque;

import org.apache.commons.pool2.PooledObjectState;

/**
* <p>
* default pooled object
* </p>
* @author linanmiao
* @date 2017年3月23日
* @versio 1.0
*/
public class SDefaultPooledObject<T> extends SPooledObject<T>{
	
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
	public int compareSTo(SPooledObject<T> other) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public T getSObject() {
		return this.object;
	}

	@Override
	public long getSCreateTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getSActiveTimeMillis() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getSIdleTimeMillis() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getSLastBorrowTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getSLastReturnTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getSLastUsedTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean startSEvictionTest() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean endSEvictionTest(Deque<SPooledObject<T>> idleQueue) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean allocate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deallocate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void invalidate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLogAbandoned(boolean logAbandoned) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void use() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printStackTrace(PrintWriter writer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PooledObjectState getState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void markAbandoned() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void markReturning() {
		// TODO Auto-generated method stub
		
	}

}
