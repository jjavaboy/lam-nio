package lam.queue.impl;

import lam.queue.LQueue;

/**
* <p>
* link queue
* </p>
* @author linanmiao
* @date 2017年3月27日
* @version 1.0
*/
public class LLinkedQueue<E> implements LQueue<E>{
	
	//type int means that max value is Integer.MAX_VALUE
	private int capacity;
	
	private LNode<E> first;
	private LNode<E> last;
	
	@Override
	public int size() {
		return capacity;
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
			throw new RuntimeException("Capacity reaches the maximun(" + Integer.MAX_VALUE + ")");
		}
		final LNode<E> l = last;
		LNode<E> newOne = new LNode<E>(last, e, null);
		this.last = newOne;
		if(l == null){
			this.first = newOne;
		}else{
			l.next = newOne;
		}
		capacity++;
	}

	@Override
	public E poll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public E unLinkFirst() {
		// TODO Auto-generated method stub
		return null;
	}

	//node to store element of queue=============================
	private class LNode<E>{
		private E value;
		private LNode<E> previous;
		private LNode<E> next;
		
		public LNode(LNode<E> previous, E value, LNode<E> next){
			this.previous = previous;
			this.value = value;
			this.next = next;
		}
	}
	
}
