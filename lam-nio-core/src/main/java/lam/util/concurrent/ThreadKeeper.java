package lam.util.concurrent;

import java.util.Queue;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年11月23日
* @version 1.0
*/
public interface ThreadKeeper {
	
	public void set(Thread t);
	
	public Queue<Thread> getQueue();

}
