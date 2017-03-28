package lam.queue;
/**
* <p>
* bidirectional queue
* </p>
* @author linanmiao
* @date 2017年3月28日
* @version 1.0
*/
public interface LDeque<E> extends LQueue<E>{
	
	/**
	 * this method is equivalent to method addLast().
	 */
	public boolean add(E e);
	
	/**
	 * this method is equivalent to method pollFirst(). 
	 */
	public E poll();
	
	/**
	 * add element e to the first of queue
	 * @param e element
	 */
	public boolean addFirst(E e);
	
	/**
	 * add element e to the last of queue
	 * @param e
	 */
	public boolean addLast(E e);
	
	/**
	 * take the first element from queue and remove it, or return null if the queue is empty.
	 */
	public E pollFirst();
	
	/**
	 * take the last element from queue and remove it, or return null if the queue is empty. 
	 */
	public E pollLast();
	
	public int size();
	
	public boolean isEmpty();
	

}
