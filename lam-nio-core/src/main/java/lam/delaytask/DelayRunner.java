package lam.delaytask;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

import lam.util.concurrent.ThreadFactoryBuilder;

/**
* <p>
* runner executes delayed task
* </p>
* @author linanmiao
* @date 2017年4月11日
* @versio 1.0
*/
public class DelayRunner {
	
	private final ScheduledThreadPoolExecutor runner = 
			new ScheduledThreadPoolExecutor(
					1, 
					new ThreadFactoryBuilder().setThreadNamePrefix("DelayRunner").build(),
					new ThreadPoolExecutor.DiscardPolicy()
					);

}
