package lam.delaytask;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lam.delaytask.support.Segment;

/**
* <p>
* Delay segment<br/>
* Each segment contains some tasks or not one.<br/>
* </p>
* @author linanmiao
* @date 2017年4月12日
* @version 1.0
*/
public class DelaySegment extends ReentrantLock implements Segment{
	
	private static final long serialVersionUID = -857493008603622475L;
	
	private static Logger logger = LoggerFactory.getLogger(DelaySegment.class);
	
	private final int slot;
	
	private Set<Task> tasks;
	
	public DelaySegment(final int slot){
		this.slot = slot;
		tasks = new HashSet<Task>();
	}

	@Override
	public int getSlot() {
		return this.slot;
	}

	@Override
	public boolean addTask(Task task) {
		lock();
		try{
			return tasks.add(task);
		}finally{
			unlock();
		}
	}

	@Override
	public void doTask() {
		if(tasks.isEmpty()){
			logger.info(String.format("segment in slot %d has not task.", this.slot));
			return ;
		}
		lock();
		try{
			final Random r = new Random();
			Iterator<Task> iter = tasks.iterator();
			while(iter.hasNext()){
				final Task task = iter.next();
				if(task.canDo()){
					iter.remove();
					TaskWorkPoolFactory.submit(new Runnable(){
						@Override
						public void run() {							
							task.doTaskSelf(r.nextInt(150));
						}
					});
				}else{					
					task.runOneTime();
				}
			}
		}finally{
			unlock();
		}
	}
	
	@Override
	public String toString() {
		String s = String.format("DelaySegment{slot:%d, tasks:%s}", this.slot, this.tasks);
		return s;
	}
	
	public static class DelayTask implements Segment.Task{
		
		private final int id;
		
		private final int segmentSlot;
		
		private final int originalCycle;
		
		private volatile int cycleNum;//current cycle number, Gaurded by countLock
		
		private final Object countLock = new Object();
		
		public DelayTask(final int id, final int cycle, final int segmentSlot){
			this.id = id;
			this.originalCycle = cycle;
			this.cycleNum = this.originalCycle;//initialize current cycle to be original cycle
			this.segmentSlot = segmentSlot;
		}
		
		@Override
		public int getId() {
			return this.id;
		}

		@Override
		public int getSegmentSlot() {
			return this.segmentSlot;
		}
		
		@Override
		public int getOriginalCycle() {
			return this.originalCycle;
		}

		@Override
		public int getCycleNum() {
			return this.cycleNum;
		}

		@Override
		public boolean canDo() {
			return cycleNum == 0;
		}
		
		@Override
		public void runOneTime() {
			if(cycleNum > 0){
				synchronized (countLock) {
					if(cycleNum > 0){
						cycleNum--;						
					}
				}
			}
		}

		@Override
		public boolean doTaskSelf(long taskTimeMillis) {
			sleep(taskTimeMillis);
			logger.info(String.format("doTask-index:%d-%s-taskTimeMillis:%d", getSegmentSlot(), toString(), taskTimeMillis));
			return true;
		}
		
		private void sleep(long millisecond){
			try {
				TimeUnit.MILLISECONDS.sleep(millisecond);
			} catch (InterruptedException e) {
				logger.error(String.format("sleeping for %d, but to be interrupted by other thread", millisecond), e);
			}
		}
		
		@Override
		public boolean equals(Object obj) {
			if(!(obj instanceof Task)){
				return false;
			}
			Task that = (Task) obj;
			return this.id == that.getId();
		}
		
		@Override
		public int hashCode() {
			return id;
		}
		
		@Override
		public String toString() {
			String s = String.format("Task{id:%d, cycleNum:%d, segmentSlot:%d, originalCycle:%d}", this.id, this.cycleNum, this.segmentSlot, this.originalCycle);
			return s;
		}
		
	}

}
