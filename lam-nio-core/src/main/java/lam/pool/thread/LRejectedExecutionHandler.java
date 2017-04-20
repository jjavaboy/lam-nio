package lam.pool.thread;
/**
* <p>
* reject interface
* </p>
* @author linanmiao
* @date 2017年4月20日
* @versio 1.0
*/
public interface LRejectedExecutionHandler {
	
	public void rejectedExecution(Runnable r, LThreadPoolExecutor executor);

}
