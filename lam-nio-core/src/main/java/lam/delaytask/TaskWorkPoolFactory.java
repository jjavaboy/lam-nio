package lam.delaytask;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lam.util.Strings;
import lam.util.concurrent.ThreadFactoryBuilder;

/**
* <p>
* dely task work pool factory
* </p>
* @author linanmiao
* @date 2017年4月13日
* @version 1.0
*/
public class TaskWorkPoolFactory{

	private static Logger logger = LoggerFactory.getLogger(TaskWorkPoolFactory.class);
	
	private static final int corePoolSize;
	
	static{
		String delayTaskWorkerPoolSize = System.getProperty("delayTaskWorkerPoolSize");
		if(Strings.isBlank(delayTaskWorkerPoolSize)){
			corePoolSize = Runtime.getRuntime().availableProcessors() + 1;
		}else{
			corePoolSize = Strings.intValue(delayTaskWorkerPoolSize, Runtime.getRuntime().availableProcessors());
		}
	}
	
	private static ThreadPoolExecutor delayTaskWorkerPool = new ThreadPoolExecutor(
			corePoolSize, Integer.MAX_VALUE, 0, TimeUnit.MILLISECONDS, 
			new LinkedBlockingQueue<Runnable>(), 
			new ThreadFactoryBuilder().setThreadNamePrefix("DelayTask").build(),
			new ThreadPoolExecutor.DiscardPolicy());
	
	public static void submit(Runnable task){
		delayTaskWorkerPool.submit(task);
	}
	
	public static void close() {
		delayTaskWorkerPool.shutdown();
		logger.info("delayTaskWorkerPool is closed.");
	}
	
}
