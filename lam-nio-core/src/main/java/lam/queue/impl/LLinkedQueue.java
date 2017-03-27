package lam.queue.impl;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import lam.queue.LQueue;

/**
* <p>
* link queue:add element to last of queue, task element from first of queue.
* </p>
* @author linanmiao
* @date 2017年3月27日
* @version 1.0
*/
public class LLinkedQueue<E> implements LQueue<E>{
	
	//type int means that max value is Integer.MAX_VALUE
	private int capacity;
	
	//to memory the add count or remove count
	private int modifyCount;
	
	private LNode<E> first;
	private LNode<E> last;
	
	@Override
	public int size() {
		return capacity;
	}
	
	@Override
	public boolean isEmpty() {
		return this.size() == 0;
	}
	
	@Override
	public E get(int index) {
		if(size() == 0 || index > size() - 1){
			return null;
		}
		LNode<E> ln = node(index);
		return ln == null ? null : ln.value;
	}
	
	@Override
	public boolean remove(int index) {
		if(size() == 0 || index > size() - 1){
			return false;
		}
		LNode<E> ln = node(index);
		return ln == null ? null : unLink(ln);
	}
	
	@Override
	public boolean remove(E e) {
		if(size() == 0){
			return false;
		}
		return unLink(e);
	}
	
	private boolean unLink(E value){
		for(LNode<E> f = first; f.next != null; f = f.next){
			if(value.equals(f.value)){
				return unLink(f);
			}
		}
		return false;
	}
	
	boolean unLink(LNode<E> f){
		f.value = null;
		final LNode<E> n = f.next;
		final LNode<E> p = f.previous;
		if(n == null){
			last = null;
		}else{
			n.previous = p;
			f.next = null;
		}
		if(p == null){
			first = null;
		}else{
			p.next = n;
			f.previous = null;
		}
		capacity--;
		modifyCountIncr();
		return true;
	}

	@Override
	public boolean add(E e) {
		if(this.size() == Integer.MAX_VALUE){
			return false;
		}
		linkLast(e);
		return true;
	}

	@Override
	public void linkLast(E e) {
		if(this.size() == Integer.MAX_VALUE){
			throw new IndexOutOfBoundsException("Capacity reaches the maximun(" + Integer.MAX_VALUE + ")");
		}
		final LNode<E> l = this.last;
		LNode<E> newNode = new LNode<E>(l, e, null);//new LNode(previous, value, next)
		this.last = newNode;
		if(this.first == null){
			this.first = newNode;
		}else{
			l.next = newNode;
		}
		capacity++;
		modifyCountIncr();
	}

	@Override
	public E poll() {
		if(this.isEmpty()){
			return null;
		}
		return unLinkFirst();
	}

	@Override
	public E unLinkFirst() {
		if(this.isEmpty()){
			throw new IndexOutOfBoundsException("Capacity is already zero");
		}
		final LNode<E> f = this.first;
		final E value = f.value;
		
		this.first = f.next;
		f.previous = null;
		f.value = null;
		if(f.next == null){
			this.last = null;
		}else{			
			f.next.previous = null;
		}
		capacity--;
		modifyCountIncr();
		return value;
	}
	
	LNode<E> node(int index){
		int lastIndex = -1;
		if((lastIndex = size() - 1) < 0 || index > lastIndex){
			throw new IndexOutOfBoundsException("index:" + index + " is greater than last index:" + lastIndex);
		}
		LNode<E> f = first;
		for(int idx = 0; idx < index; idx++){
			f = f.next;
		}
		return f;
	}
	
	private void modifyCountIncr(){
		modifyCount++;
	}
	
	@Override
	public Iterator<E> iterator() {
		System.out.println("iterator()");
		return new QueueIterator();
	}

	//node to store element of queue=============================
	static class LNode<E>{
		E value;
		LNode<E> previous;
		LNode<E> next;
		
		public LNode(LNode<E> previous, E value, LNode<E> next){
			this.previous = previous;
			this.value = value;
			this.next = next;
		}
	}
	
	//queue iterator=========================
	private class QueueIterator implements Iterator<E>{
		
		/**
		 * last time to find the index
		 */
		private int lastIdx = -1;
		
		/**
		 * indicate the next index
		 */
		private int cursor = 0;
		
		private int expectedModifyCount = modifyCount;

		@Override
		public boolean hasNext() {
			System.out.println("hasNext()");
			return cursor != size();
		}

		@Override
		public E next() {
			System.out.println("next()");
			checkIfModify();
			try{
				int currentIndex = cursor;
				E ln = get(currentIndex);
				lastIdx = currentIndex;
				cursor++;
				return ln;
			}catch(IndexOutOfBoundsException e){
				throw new NoSuchElementException("elements which index is " + lastIdx + " do not exists");
			}
		}

		@Override
		public void remove() {
			System.out.println("remove()");
			if(lastIdx < 0){
				throw new IllegalStateException("remove() should be called after next()");
			}
			checkIfModify();
			try{
				LLinkedQueue.this.remove(lastIdx);
				if(lastIdx < cursor){
					cursor--;
				}
				
				//
				lastIdx = -1;
				
				//reset expectedModifyCount
				expectedModifyCount = modifyCount;
			}catch(IndexOutOfBoundsException e){
				throw new ConcurrentModificationException();
			}
		}
		
		private void checkIfModify(){
			if(expectedModifyCount != modifyCount){
				throw new ConcurrentModificationException("queue has been modified!");
			}
		}
		
	}
	
}
