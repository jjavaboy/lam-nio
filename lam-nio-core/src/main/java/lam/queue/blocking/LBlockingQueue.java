package lam.queue.blocking;

import java.util.concurrent.TimeUnit;

/**
* <p>
* blocking queue interface
* </p>
* @author linanmiao
* @date 2017年3月28日
* @version 1.0
*/
public interface LBlockingQueue<E> extends Iterable<E>{
	
	/**
	 * insert an element
	 */
	public boolean offer(E e);
	
	/**
	 * insert an element, waiting up to ${timeout} if necessary for space to become available. 
	 */
	public boolean offer(E e, long timeout, TimeUnit timeUnit) throws InterruptedException;
	
	public E poll();
	
	/**
	 * take and remove the first element of this queue, 
	 * waiting up to ${timeout} if necessary for an element to become available
	 */
	public E poll(long timeout, TimeUnit timeUnit) throws InterruptedException;
	
	public boolean remove(E e);
	
	public int size();
	
	public boolean isEmpty();
	
}
