package lam.delaytask;

import java.io.Closeable;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lam.delaytask.support.Runner;
import lam.delaytask.support.Segment;
import lam.delaytask.support.Segment.Task;
import lam.util.FinalizeUtils;
import lam.util.concurrent.ThreadFactoryBuilder;
import lam.util.support.Startable;

/**
* <p>
* runner executes delayed task
* </p>
* @author linanmiao
* @date 2017年4月11日
* @versio 1.0
*/
public class DelayTaskRunner implements Runner, Startable, Closeable{
	
	private static Logger logger = LoggerFactory.getLogger(DelayTaskRunner.class);
	
	private final ScheduledThreadPoolExecutor runner = 
			new ScheduledThreadPoolExecutor(
					1, 
					new ThreadFactoryBuilder().setThreadNamePrefix("DelayTaskRunner").build(),
					new ThreadPoolExecutor.DiscardPolicy()
					);

	private final Segment[] segments; //It will be guarded by segmentsLock.
	
	private final Object segmentsLock = new Object();
	
	private volatile int currentIndex = -1; //It will be guarded by indexLock.
	
	private final Object indexLock = new Object();
	
	private final int firstIndex = 0;
	
	private final int lastIndex;
	
	private final static long DEFAULT_TIME_INTERVAL = 1000; //millisecond
	
	private final static TimeUnit DEFAULT_TIME_UNIT = TimeUnit.MILLISECONDS;
	
	private final long timeInterval;
	
	private final TimeUnit timeUnit;
	
	private volatile boolean close;
	
	public DelayTaskRunner(){
		this(3600, DEFAULT_TIME_INTERVAL, DEFAULT_TIME_UNIT);
	}
	
	private DelayTaskRunner(int nSegments, long timeInterval, TimeUnit timeUnit){
		if(nSegments <= 0){
			throw new IllegalArgumentException("nSegments(" + nSegments + ") must be positive");
		}
		if(timeInterval <= 0){
			throw new IllegalArgumentException("timeInterval(" + timeInterval + ") must be positive");
		}
		this.timeInterval = timeInterval;
		this.timeUnit = timeUnit;
		segments = new Segment[nSegments];
		lastIndex = segments.length - 1;
	}
	
	@Override
	public void close() {
		close = Boolean.TRUE.booleanValue();
		TaskWorkPoolFactory.close();
		runner.shutdown();
		logger.info(getClass().getName() + " close");
	}

	@Override
	public void start() {
		if(isClose()){
			throw new IllegalStateException("this object has been closed.");
		}
		runner.scheduleAtFixedRate(new DelayRunnable(), timeInterval, timeInterval, timeUnit);
		logger.info(getClass().getName() + " start");
	}

	@Override
	public boolean addTask(final Task task) {
		if(isClose()){
			logger.info("this object has been closed, task can not be added any more.");
			return false;
		}
		if(task.getSegmentSlot() < 0 || task.getSegmentSlot() > lastIndex){
			logger.info("This segment slot(" + task.getSegmentSlot() + ") of task is out of range(0 - " + lastIndex + ")");
			return false;
		}
		if(segments[task.getSegmentSlot()] == null){
			synchronized (segmentsLock) {
				if(segments[task.getSegmentSlot()] == null){
					segments[task.getSegmentSlot()] = createSegment(task.getSegmentSlot());
					logger.info("add segment in slot:" + task.getSegmentSlot());
				}
			}
		}
		logger.info(String.format("addTask-Segment:slot:%d,%s", task.getSegmentSlot(), task));
		return segments[task.getSegmentSlot()].addTask(task);
	}

	@Override
	public Segment nextSegment() {
		if(isClose()){
			throw new IllegalStateException("this object has been closed.");
		}
		int cIndex = getCurrentIndex();
		return segments[cIndex];
	}
	
	@Override
	public int second2SegmentSlot(int second) {
		final int cIndex = currentIndex;
		if(cIndex + second > lastIndex){//cIndex:1,second:3599==>>slot:1,lastIndex:3600
			second = (cIndex + 1 + second) % this.segments.length;
			if(second == 0){
				return lastIndex;
			}
		}else{
			second = cIndex + second;
		}
		return second - 1;
	}
	
	@Override
	public Segment getSegment(int index) {
		if(isClose()){
			throw new IllegalStateException("this object has been closed.");
		}
		return segments[index];
	}
	
	@Override
	public boolean remove(Segment segment) {
		if(isClose()){
			return false;
		}
		synchronized (segmentsLock) {			
			FinalizeUtils.closeQuietly(segment);
			segments[segment.getSlot()] = null;
			logger.info("remove segment in slot:" + segment.getSlot());
		}
		return true;
	}
	
	private Segment createSegment(int slot){
		return new DelaySegment(slot);
	}
	
	private int getCurrentIndexFitly(){
		synchronized (indexLock) {
			currentIndex++;
			if(currentIndex > lastIndex){
				currentIndex = firstIndex;
			}
			return currentIndex;
		}
	}
	
	public int getCurrentIndex(){
		synchronized (indexLock) {			
			return this.currentIndex < 0 ? 0 : this.currentIndex;
		}
	}

	@Override
	public void doTask() {
		if(isClose()){
			logger.info("this object has been closed, cancel the task.");
			return ;
		}
		//Segment segment = nextSegment();
		int currentIndex = getCurrentIndexFitly();
		Segment segment = getSegment(currentIndex);
		if(segment != null){
			segment.doTask();
			if(segment.isEmpty()){
				remove(segment);
			}
		}else{
			logger.info(String.format("doTask-index:%d-%s", currentIndex, segment));				
		}
	}
	
	@Override
	public boolean isClose() {
		return close;
	}
	
	private class DelayRunnable implements Runnable{
		@Override
		public void run() {
			if(isClose()){
				logger.info("DelayTaskRunner object is closed, so cancel the thread.");
				return ;
			}
			doTask();
		}
	}
	
}
