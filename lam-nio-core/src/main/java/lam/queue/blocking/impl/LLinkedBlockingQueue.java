package lam.queue.blocking.impl;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import lam.queue.blocking.LBlockingQueue;

/**
* <p>
* linked blocking queue
* </p>
* @author linanmiao
* @date 2017年3月28日
* @version 1.0
*/
public class LLinkedBlockingQueue<E> implements LBlockingQueue<E>{
	
	/**
	 * This queue can store how many elements.
	 */
	private final int capacity;
	
	/**
	 * How many elements has been store currently.
	 */
	private final AtomicInteger count = new AtomicInteger(0);
	
	private volatile LNode<E> head;
	
	private volatile LNode<E> last;
	
	/**
	 * lock for write operation
	 */
	private final ReentrantLock putLock = new ReentrantLock();
	
	private final Condition notFull = putLock.newCondition();
	
	/**
	 * lock for read operation
	 */
	private final ReentrantLock takeLock = new ReentrantLock();
	
	private final Condition notEmpty = takeLock.newCondition();
	
	
	public LLinkedBlockingQueue(){
		this(Integer.MAX_VALUE);
	}
	
	public LLinkedBlockingQueue(int capacity){
		if(capacity < 0){
			throw new IllegalArgumentException("capacity is negative");
		}
		this.capacity = capacity;
		head = last = new LNode<E>(null);
	}

	@Override
	public Iterator<E> iterator() {
		return new BlockingQueueIterator();
	}

	@Override
	public boolean offer(E e) {
		if(e == null){
			throw new NullPointerException("element is null");
		}
		if(size() == capacity){
			return false;
		}
		int oldCount = -1;
		//get write lock
		putLock.lock();
		try{
			//double check
			if(size() < capacity){
				LNode<E> newNode = new LNode<E>(e);
				enqueue(newNode);
				oldCount = count.getAndIncrement();
				if(oldCount + 1 < capacity){
					notFull.signalAll();
				}
			}
		}finally{
			putLock.unlock();
		}
		if(oldCount == 0){
			signalNotEmpty();
		}
		//It means element e has been added into this queue
		return oldCount != -1;
	}
	
	void enqueue(LNode<E> ln){
		last = last.next = ln;
	}
	
	E dequeue(){
		LNode<E> first = head.next;
		LNode<E> newFirst = first.next;
		head.next = newFirst;
		if(newFirst == null){
			last = head;
		}
		final E e = first.item;
		first.item = null;
		first.next = null;
		return e;
	}
	
	private void signalNotEmpty(){
		takeLock.lock();
		try{
			notEmpty.signalAll();
		}finally{
			takeLock.unlock();
		}
	}
	
	private void signalNotFull(){
		putLock.lock();
		try{
			notFull.signalAll();
		}finally{
			putLock.unlock();
		}
	}

	@Override
	public boolean offer(E e, long timeout, TimeUnit timeUnit) throws InterruptedException {
		if(e == null){
			throw new NullPointerException("element is null");
		}
		int oldCount = -1;
		long nanos = timeUnit.toNanos(timeout);
		final ReentrantLock pLock = putLock;
		pLock.lockInterruptibly();
		try{
			while(size() == capacity){
				if(nanos <= 0){
					return false;
				}
				nanos = notFull.awaitNanos(nanos);
			}
			enqueue(new LNode<E>(e));
			oldCount = count.getAndIncrement();
			if(oldCount + 1 < capacity){
				notFull.signalAll();
			}
		}finally{
			pLock.unlock();
		}
		if(oldCount == 0){
			signalNotEmpty();
		}
		return true;
	}

	@Override
	public E poll() {
		if(isEmpty()){
			return null;
		}
		int oldCount = -1;
		E e = null;
		final ReentrantLock tLock = takeLock;
		tLock.lock();
		try{
			if(!isEmpty()){
				e = dequeue();
				oldCount = count.getAndDecrement();
				if(oldCount > 1){
					notEmpty.signalAll();
				}
			}
		}finally{
			tLock.unlock();
		}
		if(oldCount == capacity){
			signalNotFull();
		}
		return e;
	}

	@Override
	public E poll(long timeout, TimeUnit timeUnit) throws InterruptedException {
		int oldCount = -1;
		E e = null;
		final ReentrantLock tLock = takeLock;
		tLock.lockInterruptibly();
		try{
			long nanos = timeUnit.toNanos(timeout);
			while(isEmpty()){
				if(nanos <= 0){
					return null;
				}
				nanos = notEmpty.awaitNanos(nanos);
			}
			e = dequeue();
			oldCount = count.getAndDecrement();
			if(oldCount > 1){
				notEmpty.signalAll();
			}
		}finally{
			tLock.unlock();
		}
		if(oldCount == capacity){
			signalNotFull();
		}
		return e;
	}

	@Override
	public int size() {
		return count.get();
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}
	
	@Override
	public boolean remove(E e) {
		if(e == null){
			return false;
		}
		lockFully();
		try{
			//head -> ... -> last
			for(LNode<E> previous = head, p = previous.next; p != null; previous = p, p = previous.next){
				if(e.equals(p.item)){
					unlink(p, previous);
					return true;
				}
			}
			return false;
		}finally{
			unlockFully();
		}
	}
	
	void unlink(LNode<E> p, LNode<E> previous){
		//p:previous.next
		
		p.item = null;
		if(p == last){
			//p.next:null
			previous.next = null;
			last = previous;
		}else{
			previous.next = p.next;
		}
		if(count.getAndDecrement() == capacity){
			notFull.signalAll();
		}
	}
	
	private void lockFully(){
		putLock.lock();
		takeLock.lock();
	}
	
	private void unlockFully(){
		takeLock.unlock();
		putLock.unlock();
	}
	
	//QueueNode=================
	static class LNode<E>{
		E item;
		LNode<E> next;
		LNode(E e){
			this.item = e;
		}
	}
	
	//BlockingQueueIterator=============
	private class BlockingQueueIterator implements Iterator<E>{
		
		private LNode<E> lastNode;
		
		/**
		 * point to the next node
		 */
		private LNode<E> currentNode;
		
		/**
		 * point to the next element
		 */
		private E currentElement;
		
		BlockingQueueIterator(){
			lockFully();
			try{
				currentNode = head.next;
				currentElement = currentNode == null ? null : currentNode.item;
			}finally{
				unlockFully();
			}
		}

		@Override
		public boolean hasNext() {
			//last.next is null, it means the end of queue.
			return currentNode != null;
		}
		
		LNode<E> nextNode(LNode<E> ln){
			if(ln == null || ln.next == null){
				return null;
			}
			return ln.next;
		}

		@Override
		public E next() {
			lockFully();
			try{
				if(currentNode == null){
					throw new NoSuchElementException("Has reached the end of the queue");
				}
				final E e = currentElement;
				final LNode<E> current = currentNode;
				lastNode = current;
				currentNode = nextNode(currentNode);
				currentElement = currentNode == null ? null : currentNode.item;
				return e;
			}finally{
				unlockFully();
			}
		}

		@Override
		public void remove() {
			lockFully();
			try{
				if(lastNode == null){
					//After call next(), lastNode means lastest node, 
					//but currentNode means next time called next() to get the lastest node. 
					throw new IllegalStateException("remove() should be called after next()");
				}
				for(LNode<E> previous = head, p = head.next; p != null; previous = p, p = previous.next){
					if(lastNode == p){
						unlink(p, previous);
						break;
					}
				}
				//reset lastNode to the initial state
				lastNode = null;
			}finally{
				unlockFully();
			}
		}
		
	}

}
