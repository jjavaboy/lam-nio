package lam.queue.blocking.impl;

import java.util.Iterator;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean offer(E e) {
		if(e == null){
			throw new NullPointerException("element is null");
		}
		if(count.get() == capacity){
			return false;
		}
		int oldCount = -1;
		//get write lock
		putLock.lock();
		try{
			//double check
			if(count.get() < capacity){
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public E poll() {
		if(size() == 0){
			return null;
		}
		int oldCount = -1;
		E e = null;
		final ReentrantLock tLock = takeLock;
		tLock.lock();
		try{
			if(size() > 0){
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		return count.get();
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}
	
	//QueueNode=================
	static class LNode<E>{
		E item;
		LNode<E> next;
		LNode(E e){
			this.item = e;
		}
	}

}
