import java.io.Serializable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import lam.dao.id.util.IdSequence;
import lam.dao.sample.model.Foo;
import lam.util.concurrent.ThreadFactoryBuilder;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年8月10日
* @version 1.0
*/
public class IdSequenceTest {
	
	private static ClassPathXmlApplicationContext context;
	
	private static ThreadPoolExecutor executor;
	
	private static Logger logger = LoggerFactory.getLogger(IdSequenceTest.class);
	
	static{
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				logger.info("start to clear resource");
				if(context != null)
					context.close();
				if(executor != null)
					executor.shutdown();
				logger.info("end to clear resource");
			}
		});
	}
	
	public static void main(String[] args){
		context = new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml"});
		context.start();
		executor = new ThreadPoolExecutor(20, 20, 0, TimeUnit.MICROSECONDS, 
				new LinkedBlockingQueue<Runnable>(),
				new ThreadFactoryBuilder().setThreadNamePrefix("IdSequenceTest").build(),
				new RejectedExecutionHandler() {
					@Override
					public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
						logger.info("reject runnable:" + r);
					}
				});
		//init the thread into pool
		for(int i = 0; i < executor.getMaximumPoolSize(); i++){
			executor.execute(new Runnable(){
				@Override
				public void run() {
				}});
		}
		
		final ConcurrentHashMap<Long, Boolean> idMap = new ConcurrentHashMap<Long, Boolean>();
		
		int idCount = 100000;
		/*CompletionService*/ExecutorCompletionService<String> completionService = new ExecutorCompletionService<String>(executor);
		LinkedBlockingQueue<Future<String>> resultQueue = new LinkedBlockingQueue<Future<String>>();
		final CountDownLatch latch = new CountDownLatch(idCount);
		long start = System.currentTimeMillis();
		for(int i = 0; i < idCount; i++){
			resultQueue.offer(
			/*completionService*/executor.submit(new Runnable(){
				@Override
				public void run() {
					long id = IdSequence.getInstance().nextId(Foo.class);
					if(idMap.containsKey(id)){
						logger.info(Thread.currentThread().getName() + "-" + "duplicate id:" + id);
					}else{
						idMap.put(id, false);
						logger.info(Thread.currentThread().getName() + "-" + "unique id:" + id);
					}
					latch.countDown();
				}
			}, "task " + i + " callback")
			);
		}
		
		//to check whether task has been done
		try {
			/*for(int j = 0; j < 100000; j++){
				//completionService.take();
				resultQueue.take().get();
			}*/
			latch.await();
			long end = System.currentTimeMillis();
			logger.info("cost(ms):" + (end - start));
		} catch (Exception e) {
			logger.error("error", e);
		}
		
		logger.info("Demo id:" + IdSequence.getInstance().nextId(Demo.class));
		
		System.exit(0);
	}
	
	private static class Demo implements Serializable{

		private static final long serialVersionUID = -6486479385581032575L;
		
	}

}
