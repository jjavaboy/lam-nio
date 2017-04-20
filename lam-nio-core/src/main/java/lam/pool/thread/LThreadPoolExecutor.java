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
	
	private volatile LRejectedExecutionHandler rejectedExecutionHandler;
	
	private final HashSet<Worker> workers = new HashSet<Worker>();
	
	private final ReentrantLock mainLock = new ReentrantLock();
	
	private volatile boolean allowCoreThreadTimeOut;
	
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
			BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, LRejectedExecutionHandler rejectedExecutionHandler){
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
		
		int c = ctl.get();
		if(workCountOf(c) < corePoolSize){
			if(addWorker(command, true)){
				return ;
			}
			c = ctl.get();
		}
		if(runStateOf(c) == RUNNING && workQueue.offer(command)){//add task to the queue
			//remove the task when ThreadPoolExecutor is not running state
			if(runStateOf(c) != RUNNING && workQueue.remove(command)){
				reject(command);
			}else if(workCountOf(ctl.get()) < corePoolSize){
				addWorker(null, false);
			}
		}else if(!addWorker(command, false)){//create new thread until thread number equals to maxinumPoolSize
											 //when fail to add task to the queue.
			reject(command);
		}
	}
	
	void reject(Runnable command){
		rejectedExecutionHandler.rejectedExecution(command, this);
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
		Worker worker = null;
		try{
			worker = new Worker(firstTask);
			final Thread thread = worker.thread;
			if(thread != null){
				lock.lock();
				try{
					//recheck the runState
					int c = ctl.get();
					int rs = runStateOf(c);
					if(rs < SHUTDOWN || (rs == SHUTDOWN && firstTask == null)){
						if(thread.isAlive()){
							throw new IllegalStateException("thread is alive, addWorker fail");
						}
						workers.add(worker);
						workerAdded = Boolean.TRUE.booleanValue();
					}
				}finally{
					lock.unlock();
				}
				if(workerAdded){
					thread.start();
					workerStarted = Boolean.TRUE.booleanValue();
				}
			}
		}finally{
			if(!workerStarted){
				addWorkderFail(worker);
			}
		}
		return workerStarted;
	}
	
	private void addWorkderFail(Worker worker){
		final ReentrantLock lock = this.mainLock;
		lock.lock();
		try{
			if(worker != null){
				workers.remove(worker);
			}
			ctl.decrementAndGet();
		}finally{
			lock.unlock();
		}
	}
	
	private final void runWorker(Worker worker){
		Thread cThread = Thread.currentThread();
		Runnable task = worker.firstTask;
		worker.firstTask = null;
		try{
			while( task != null || (task = getTask()) != null){
				if(runStateOf(ctl.get()) >= STOP){
					cThread.interrupt();
				}
				beforeExecute(cThread, task);
				Throwable t = null;
				try{
					task.run();
				}catch(Throwable t1){
					t = t1; throw t1;
				}finally{
					afterExecute(t, task);
				}
			}
		}finally{
			
		}
	}
	
	protected void beforeExecute(Thread t, Runnable task){ }
	
	protected void afterExecute(Throwable t, Runnable task){ }
	
	private Runnable getTask(){
		boolean timeOut = Boolean.FALSE.booleanValue();
		retry:
		while(true){
			int c = ctl.get();
			int rs = runStateOf(c);
			
			//ThreadPoolExecutor is shutdown and workQueue is emtpy, or it is stop, then return null.
			if(rs >= SHUTDOWN && (rs >= STOP || workQueue.isEmpty())){
				ctl.decrementAndGet();
				return null;
			}
			
			boolean timed = Boolean.FALSE.booleanValue();
			while(true){
				int wc = workCountOf(c);
				timed = this.allowCoreThreadTimeOut || wc > corePoolSize;
				if(wc <= maxinumPoolSize && !(timeOut && timed)){
					break;
				}
				if(ctl.compareAndSet(c, c - 1)){
					return null;
				}
				c = ctl.get();
				//It means state has changed
				if(runStateOf(c) != rs){
					continue retry;
				}
			}
			
			try {
				Runnable r = timed ? 
						workQueue.poll(keepAliveTime, TimeUnit.NANOSECONDS) : workQueue.take();
				if(r != null){
					return r;
				}
				timeOut = true;
			} catch (InterruptedException e) {
				timeOut = false;
			}
		}
	}
	
	public ThreadFactory getThreadFactory() {
		return threadFactory;
	}
	
	public void setAllowCoreThreadTimeOut(boolean coreThreadTimeOut) {
		if(coreThreadTimeOut && this.keepAliveTime <= 0){
			throw new IllegalArgumentException("keepAliveTime is negative");
		}
		this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
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
			runWorker(this);
		}
		
	}

}
