package lam.pool.thread;

import java.util.HashSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;

/**
* <p>
* To simulate the java.util.concurrent.ThreadPoolExecutor
* </p>
* @author linanmiao
* @date 2017年4月20日
* @version 1.0
*/
public class LThreadPoolExecutor implements Executor{
	
	//=====================================
	
	private volatile int corePoolSize;
	
	private volatile int maxinumPoolSize;
	
	private volatile long keepAliveTime;
	
	private final BlockingQueue<Runnable> workQueue;
	
	private volatile ThreadFactory threadFactory;
	
	private volatile RejectedExecutionHandler rejectedExecutionHandler;
	
	private final HashSet<Worker> workers = new HashSet<Worker>();
	
	private final ReentrantLock mainLock = new ReentrantLock();
	
	//======================================
	
	//**************************************workCount and runState
	//uses 29 bit in lower bit to store capacity
	//users 3 bit in higher bit to store state
	private static final int COUNT_BITS = Integer.SIZE - 3;

	//uses 29 bit in lower bit to store capacity
	private static final int CAPACITY = (1 << COUNT_BITS) - 1;
	
	private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));
	
	private static final int RUNNING    = -1 << COUNT_BITS;
	private static final int SHUTDOWN   = 0  << COUNT_BITS;
	private static final int STOP       = 1  << COUNT_BITS;
	private static final int TIDYING    = 2  << COUNT_BITS;
	private static final int TERMINATED = 3  << COUNT_BITS;
	
	private static int runStateOf(int c){
		return c & ~CAPACITY;//~CAPACITY:3 bit in higher bit to store state
	}
	
	private static int workCountOf(int c){
		return c & CAPACITY;//
	}
	
	private static int ctlOf(int rs, int wc){
		return rs | wc;
	}
	
	//**************************************
	
	public LThreadPoolExecutor(int corePoolSize, int maxinumPoolSize, long keepAliveTime, TimeUnit timeUnit,
			BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler){
		if(corePoolSize < 0 || maxinumPoolSize < 0 || corePoolSize > maxinumPoolSize || keepAliveTime < 0){
			throw new IllegalArgumentException("Please check argument of constructor");
		}
		
		this.corePoolSize = corePoolSize;
		this.maxinumPoolSize = maxinumPoolSize;
		this.keepAliveTime = timeUnit.toNanos(keepAliveTime);
		this.workQueue = workQueue;
		this.threadFactory = threadFactory;
		this.rejectedExecutionHandler = rejectedExecutionHandler;
	}
	
	@Override
	public void execute(Runnable command) {
		if(command == null){
			throw new NullPointerException("command is null");
		}
		
		int rs = ctl.get();
		if(workCountOf(rs) < corePoolSize){
			
		}
	}
	
	private boolean addWorker(Runnable firstTask, boolean core){
		//"retry while" is to make workCount increase concurrentlly
		retry:
		while(true){
			int c = ctl.get();
			int rs = runStateOf(c);
			if(rs >= SHUTDOWN || !(rs == SHUTDOWN && firstTask == null && !workQueue.isEmpty())){
				return false;
			}
			while(true){
				int wc = workCountOf(c);
				if(wc >= CAPACITY || wc >= (core ? corePoolSize : maxinumPoolSize)){
					return false;
				}
				//ctl increase just equals to workCount increase
				if(ctl.compareAndSet(c, c + 1)){
					break retry;
				}
				//workCount fails to increase, it means that some other thread changed ctl value before
				//then do it next
				
				//To get the new value of ctl
				c = ctl.get();
				
				//To compare the runState, so we can know whther the state has been changed?
				if(runStateOf(c) != rs){
					continue retry;
				}
			}
		}
	
		boolean workerAdded = Boolean.FALSE.booleanValue();
		boolean workerStarted = Boolean.FALSE.booleanValue();
		final ReentrantLock lock = this.mainLock;
			Worker worker = new Worker(firstTask);
			final Thread thread = worker.thread;
			if(thread != null){
				lock.lock();
				try{
				
				}finally{
					lock.unlock();
				}
			}
		return workerStarted;
	}
	
	public ThreadFactory getThreadFactory() {
		return threadFactory;
	}
	
	private class Worker extends AbstractQueuedSynchronizer implements Runnable{

		private static final long serialVersionUID = -2788282885856094079L;

		final Thread thread;
		Runnable firstTask;
		
		Worker(Runnable firstTask){
			setState(-1);
			this.firstTask = firstTask;
			this.thread = getThreadFactory().newThread(this);
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
		
	}

}
