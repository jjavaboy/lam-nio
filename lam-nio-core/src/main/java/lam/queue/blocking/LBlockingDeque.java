package lam.queue.blocking;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
* <p>
* blocking deque
* </p>
* @author linanmiao
* @date 2017年3月29日
* @version 1.0
*/
public interface LBlockingDeque<E> extends Iterable<E>{

	/**
	 * This method is equivalent to the method offerLast()
	 */
	public boolean offer(E e);
	
	public boolean offerLast(E e);
	
	public boolean offerLast(E e, long timeout, TimeUnit timeUnit)throws InterruptedException;
	
	public boolean offerFirst(E e);
	
	public boolean offerFirst(E e, long timeout, TimeUnit timeUnit)throws InterruptedException;
	
	/**
	 * Throw an IllegalStateException("Deque is full") if the deque is full;
	 */
	public boolean addFirst(E e);
	
	/**
	 * Throw an IllegalStateException("Deque is full") if the deque is full;
	 */
	public boolean addLast(E e);
	
	/**
	 * This method is equivalent to the method pollFirst()
	 */
	public E poll();
	
	public E pollFirst();
	
	public E pollFirst(long timeout, TimeUnit timeUnit)throws InterruptedException;
	
	public E pollLast();
	
	public E pollLast(long timeout, TimeUnit timeUnit)throws InterruptedException;
	
	/**
	 * This method is equivalent to the method takeFirst().
	 */
	public E take()throws InterruptedException;
	
	/**
	 * take and remove the first element of queue, waiting if necessary until an element becomes available.
	 */
	public E takeFirst()throws InterruptedException;
	
	/**
	 * take nad remove the last element of queue, waiting if necessary until an element becomes available.
	 */
	public E takeLast()throws InterruptedException;
	
	public boolean remove(E e);
	
	public int size();
	
	public boolean isEmpty();
	
	public boolean hasTakeWaiters();
	
	public int getTakeQueueLength();
	
	public Iterator<E> descendingIterator();
	
}
