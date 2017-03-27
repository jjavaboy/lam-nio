package lam.queue;
/**
* <p>
* queue interface
* </p>
* @author linanmiao
* @date 2017年3月27日
* @version 1.0
*/
public interface LQueue <E> extends Iterable<E>{
	
	/**
	 * size of queue.
	 */
	public int size();
	
	public boolean isEmpty();
	
	public E get(int index);
	
	/**
	 * remove the elements sorted in the index.
	 */
	public boolean remove(int index);
	
	public boolean remove(E e);
	
	/**
	 * this method is equivalent to the method linkLast(E e);
	 */
	public boolean add(E e);
	
	/**
	 * link the element to the last of the queue.
	 */
	public void linkLast(E e);
	
	/**
	 * this method is equivalent to the method unLinkFirst();
	 */
	public E poll();
	
	/**
	 * take the first element and remove it.
	 */
	public E unLinkFirst();

}
