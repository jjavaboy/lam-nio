package lam.pool.thread;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
* <p>
* To simulate the java.util.concurrent.ThreadPoolExecutor
* </p>
* @author linanmiao
* @date 2017年4月20日
* @version 1.0
*/
public class LThreadPoolExecutor implements ExecutorService, Executor{
	
	//=====================================
	
	private volatile int corePoolSize;
	
	private volatile int maxinumPoolSize;
	
	private volatile long keepAliveTime;
	
	private final BlockingQueue<Runnable> workQueue;
	
	private volatile ThreadFactory threadFactory;
	
	private volatile LRejectedExecutionHandler rejectedExecutionHandler;
	
	private final HashSet<Worker> workers = new HashSet<Worker>();
	
	private final ReentrantLock mainLock = new ReentrantLock();
	
	private final Condition termination = mainLock.newCondition();
	
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
	
	private static boolean runStateAtLeast(int c, int s){
		return c >= s;
	}
	
	//**************************************
	
	private static final RuntimePermission shutdownPerm = new RuntimePermission("modifyThread");
	
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
			if(rs >= SHUTDOWN && !(rs == SHUTDOWN && firstTask == null && !workQueue.isEmpty())){
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
				worker.lock();
				try{
					beforeExecute(cThread, task);
					Throwable t = null;
					try{
						task.run();
					}catch(Throwable t1){
						t = t1; throw t1;
					}finally{
						afterExecute(t, task);
					}
				}finally{
					worker.unLock();
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
	
	private void advanceRunState(int targetState) {
        for (;;) {
            int c = ctl.get();
            if (runStateAtLeast(c, targetState) //leave it alone(let it be) if it already at the targetState state 
            		||
                ctl.compareAndSet(c, ctlOf(targetState, workCountOf(c)))) //change the state to be targetState
                break;
        }
    }
	
	protected void onShutdown(){ }
	
	private void interruptIdleWorkers(){
		interruptIdleWorkers(false);
	}
	
	private void interruptIdleWorkers(boolean onlyOne){
		final ReentrantLock lock = this.mainLock;
		lock.lock();
		try{
			for(Worker worker : workers){
				if(!worker.thread.isInterrupted() && worker.tryLock()){//tryLock():retrun true, 
																	   //it means that worker is idle, maybe it just done a task or not. 
					try{
						worker.thread.interrupt();
					}catch(SecurityException  s){
						
					}finally{
						worker.unLock();
					}
				}
				if(onlyOne){
					break;
				}
			}
		}finally{
			lock.unlock();
		}
	}
	
	private void checkShutdownAccess() {
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            security.checkPermission(shutdownPerm);
            final ReentrantLock mainLock = this.mainLock;
            mainLock.lock();
            try {
                for (Worker worker : workers){
                	try{
                		//check it whether thread is a system thread, its thread group has a null parent
                		security.checkAccess(worker.thread);
                	}catch(SecurityException e){
                		System.out.println("thread:" + worker.thread + " is a system thread, so called can't access.");
                		throw e;
                	}
                }
            } finally {
                mainLock.unlock();
            }
        }
    }
	
	/**
	 * try to set runState to TIDYING, then TERMINATED
	 */
	final void tryTerminate(){
		//Just do it
		for(;;){
			int c = ctl.get();
			if(runStateOf(c) == RUNNING || runStateAtLeast(c, TIDYING) || 
					(runStateOf(c) == SHUTDOWN && !workQueue.isEmpty())){
				return ;
			}
			if(workCountOf(c) != 0){
				interruptIdleWorkers(true);
				return ;
			}
			final ReentrantLock lock = this.mainLock;
			lock.lock();
			try{
				if(ctl.compareAndSet(c, ctlOf(TIDYING, 0))){
					try{
						terminated();
					}finally{
						ctl.set(ctlOf(TERMINATED, 0));
						termination.signalAll();
					}
					return ;
				}
			}finally{
				lock.unlock();
			}
		}
	}
	
	protected void terminated(){ }
	
	private List<Runnable> drainQueue(){
		BlockingQueue<Runnable> queue = this.workQueue;
		List<Runnable> takeList = new ArrayList<Runnable>(queue.size());
		queue.drainTo(takeList);
		if(!queue.isEmpty()){
			for(Runnable r : queue.toArray(new Runnable[0])){
				queue.remove(r);
				takeList.add(r);
			}
		}
		return takeList;
	}
	
	/**
	 * The callers will interrupt all workers, even some workers is doing the task.
	 */
	private void interruptWorkers(){
		final ReentrantLock lock = this.mainLock;
		lock.lock();
		try{
			for(Worker worker : workers){
				worker.interruptIfStarted();
			}
		}finally{
			lock.unlock();
		}
	}

	@Override
	public void shutdown() {
		final ReentrantLock lock = this.mainLock;
		lock.lock();
		try{
			checkShutdownAccess();
			advanceRunState(SHUTDOWN);
			interruptIdleWorkers();
			onShutdown();
		}finally{
			lock.unlock();
		}
		tryTerminate();
	}

	@Override
	public List<Runnable> shutdownNow() {
		List<Runnable> tasks = null;
		final ReentrantLock lock = this.mainLock;
		lock.lock();
		try{
			checkShutdownAccess();
			advanceRunState(STOP);
			interruptWorkers();
			tasks = drainQueue();
		}finally{
			lock.unlock();
		}
		tryTerminate();
		return tasks;
	}

	@Override
	public boolean isShutdown() {
		return runStateOf(ctl.get()) != RUNNING;
	}

	@Override
	public boolean isTerminated() {
		return runStateOf(ctl.get()) >= TERMINATED;
	}

	@Override
	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T> Future<T> submit(Callable<T> task) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> Future<T> submit(Runnable task, T result) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Future<?> submit(Runnable task) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
			throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}
	
	//=========================Worker
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
		
		public void lock(){
			super.acquire(1);
		}
		
		public boolean tryLock(){
			return super.tryAcquire(1);
		}
		
		public void unLock(){
			super.release(1);
		}
		
		protected boolean tryAcquire(int unused) {
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        protected boolean tryRelease(int unused) {
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }
		
		public void interruptIfStarted(){
			Thread t;
			if(getState() >= 0 && (t = this.thread) != null && !t.isInterrupted()){
				try{
					t.interrupt();
				} catch (SecurityException ignore) {
	            }
			}
		}
	}

}
