import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
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
	
	private static Logger logger = LoggerFactory.getLogger(IdSequenceTest.class);
	
	static{
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				if(context != null)
					context.close();
			}
		});
	}
	
	public static void main(String[] args){
		context = new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml"});
		context.start();
		ThreadPoolExecutor executor = new ThreadPoolExecutor(8, 8, 0, TimeUnit.MICROSECONDS, 
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
		
		long start = System.currentTimeMillis();
		for(int i = 0; i < 100000; i++){
			executor.execute(new Runnable(){
				@Override
				public void run() {
					long id = IdSequence.getInstance().nextId(Foo.class.getSimpleName());
					if(idMap.containsKey(id)){
						logger.info(Thread.currentThread().getName() + "-" + "duplicate id:" + id);
					}else{
						idMap.put(id, false);
						logger.info(Thread.currentThread().getName() + "-" + "unique id:" + id);
					}
				}
			});
		}
		long end = System.currentTimeMillis();
		//logger.info("cost(ms):" + (end - start));
	}

}
