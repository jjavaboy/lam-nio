package lam.queue.impl;

import java.security.NoSuchAlgorithmException;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import lam.queue.LDeque;

/**
* <p>
* bidirctectional linked queue
* </p>
* @author linanmiao
* @date 2017年3月28日
* @version 1.0
*/
public class LLinkedDeque<E> implements LDeque<E>{
	
	private int size;
	
	/**
	 * It will increases when the queue performs an add or remove operations 
	 */
	private int modifyCount;
	
	/**
	 * the first element in the head of the queue
	 */
	private LNode<E> first;
	/**
	 * the last lelement in the tail of the queue
	 */
	private LNode<E> last;

	@Override
	public boolean add(E e) {
		return addLast(e);
	}

	@Override
	public Iterator<E> iterator() {
		return new DequeIterator();
	}

	@Override
	public boolean addFirst(E e) {
		if(size() == Integer.MAX_VALUE){
			return false;
		}
		return linkFirst(e);
	}
	
	private void checkMaxSize(){		
		if(size() == Integer.MAX_VALUE){
			throw new IndexOutOfBoundsException(String.format(
					"size:%d reaches the maximum:%d, you can't add new element any more.", size, Integer.MAX_VALUE));
		}
	}

	@Override
	public boolean addLast(E e) {
		if(size() == Integer.MAX_VALUE){
			return false;
		}
		linkLast(e);
		return true;
	}
	
	@Override
	public E poll() {
		return pollFirst();
	}

	@Override
	public E pollFirst() {
		if(isEmpty()){
			return null;
		}
		return unLinkFirst();
	}

	@Override
	public E pollLast() {
		if(isEmpty()){
			return null;
		}
		return unlinksLast();
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}
	
	@Override
	public void linkLast(E e){
		checkMaxSize();
		//the last node before the new node was added
		final LNode<E> l = last;
		LNode<E> newLast = new LNode<E>(l, e, null);
		last = newLast;
		if(first == null){
			first = newLast;
		}else{
			//new node links to the old last node
			l.next = newLast;
		}
		size++;
		modifyCountIncr();
	}
	
	boolean linkFirst(E e){
		checkMaxSize();
		//the old first node before the new node was added to the head of queue
		final LNode<E> f = first;
		LNode<E> newFirst = new LNode<E>(null, e, f);
		first = newFirst;
		if(last == null){
			//The last is null, it means queue is empty. 
			last = newFirst;
		}else{
			f.previous = newFirst;
		}
		size++;
		modifyCountIncr();
		return true;
	}
	
	E unlinksLast(){
		if(isEmpty()){
			throw new IllegalStateException("queue is emtpy");
		}
		final LNode<E> l = last;
		if(last == null){
			return null;
		}
		final E e = l.element;
		final LNode<E> pre = l.previous;
		l.previous = null;
		l.element = null;
		//pre is the new last node
		last = pre;
		if(pre == null){
			//p is null, it means that the first and the old last are both point to the same node(it is the only one node in the queue).
			first = null;
		}else{
			pre.next = null;
		}
		size--;
		modifyCountIncr();
		return e;
	}

	@Override
	public E get(int index) {
		LNode<E> ln = node(index);
		return ln == null ? null : ln.element;
	}

	@Override
	public boolean remove(int index) {
		LNode<E> f = node(index);
		return f == null ? false : remove(f);
	}

	@Override
	public boolean remove(E e) {
		if(isEmpty()){
			return false;
		}
		LNode<E> f = node(e);
		return f == null ? false : remove(f);
	}
	
	private LNode<E> node(int index){
		int lastIndex = size() - 1;
		if(isEmpty() || index > lastIndex){
			throw new IndexOutOfBoundsException(String.format(
					"queue is emtpy or index:%d is greater than last index:%d", index, lastIndex));
		}
		LNode<E> f = first;
		for(int idx = 0; idx < index; idx++){
			f = f.next;
		}
		return f;
	}
	
	private LNode<E> node(E e){
		if(isEmpty()){
			throw new IndexOutOfBoundsException("queue is empty");
		}
		for(LNode<E> f = first; f.next != null; f = f.next){
			if(e.equals(f.element)){
				return f;
			}
		}
		return null;
	}
	
	private boolean remove(LNode<E> ln){
		final LNode<E> pre = ln.previous;
		final LNode<E> nex = ln.next;
		if(pre != null){
			pre.next = nex;
		}else{
			first = null;
		}
		if(nex != null){
			nex.previous = pre;
		}else{
			last = null;
		}
		ln.previous = null;
		ln.next = null;
		ln.element = null;
		return true;
	}

	@Override
	public E unLinkFirst() {
		if(isEmpty()){
			throw new IllegalStateException("queue is emtpy");
		}
		final LNode<E> f = first;
		if(f == null){
			return null;
		}
		final E e = f.element;
		final LNode<E> nex = f.next;
		f.next = null;
		f.element = null;
		//nex is the new first node
		first = nex;
		if(nex == null){
			last = null;
		}else{
			nex.previous = null;
		}
		size--;
		modifyCountIncr();
		return e;
	}
	
	private void modifyCountIncr(){
		modifyCount++;
	}
	
	//LNode=================================
	static class LNode<E>{
		E element;
		LNode<E> previous;
		LNode<E> next;
		
		public LNode(LNode<E> previous, E element, LNode<E> next){
			this.previous = previous;
			this.element = element;
			this.next = next;
		}
	}

	//DequeIterator=========================
	private class DequeIterator implements Iterator<E>{
		
		private int lastIdx = -1;
		
		private int cursor = 0;
		
		private int expectedModifyCount = modifyCount;

		@Override
		public boolean hasNext() {
			return cursor != size();
		}

		@Override
		public E next() {
			checkModifyCount();
			try{
				int currentIndex = cursor;
				LNode<E> f = node(currentIndex);
				lastIdx = currentIndex;
				cursor++;
				return f.element;
			}catch(IndexOutOfBoundsException e){
				throw new NoSuchElementException("element of index:" + lastIdx + " do not exists");
			}
		}

		@Override
		public void remove() {
			if(lastIdx < 0){
				throw new IllegalStateException("remove() should be call after next()");
			}
			checkModifyCount();
			try{
				LLinkedDeque.this.remove(lastIdx);
				cursor--;
				//reset to the inital state
				lastIdx = -1;
				expectedModifyCount = modifyCount;
			}catch(IndexOutOfBoundsException e){
				throw new ConcurrentModificationException();
			}
		}
		
		private void checkModifyCount(){
			if(expectedModifyCount != modifyCount){
				throw new ConcurrentModificationException("modifyCount has been modified");
			}
		}
		
	}
}
