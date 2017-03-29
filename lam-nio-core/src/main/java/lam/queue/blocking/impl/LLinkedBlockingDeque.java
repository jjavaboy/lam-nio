package lam.queue.blocking.impl;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import lam.queue.LDeque;
import lam.queue.blocking.LBlockingDeque;

/**
* <p>
* linked block deque
* </p>
* @author linanmiao
* @date 2017年3月29日
* @version 1.0
*/
public class LLinkedBlockingDeque<E> implements LBlockingDeque<E>, LDeque<E>{
	
	private volatile LNode<E> first;
	private volatile LNode<E> last;
	
	/**
	 * Number of node in this deque
	 */
	private int count;
	
	/**
	 * Maximum number of node in this deque
	 */
	private final int capacity;
	
	private final ReentrantLock lock = new ReentrantLock();
	
	private final Condition notEmpty = lock.newCondition();
	
	private final Condition notFull = lock.newCondition();
	
	public LLinkedBlockingDeque(){
		this(Integer.MAX_VALUE);
	}
	
	public LLinkedBlockingDeque(int capacity){
		if(capacity <= 0){
			throw new IllegalArgumentException("capacity must be positive");
		}
		this.capacity = capacity;
	}

	@Override
	public Iterator<E> iterator() {
		return new DequeAscIterator();
	}
	
	@Override
	public Iterator<E> descendingIterator() {
		return new DequeDescendingIterator();
	}

	@Override
	public boolean offer(E e) {
		return offerLast(e);
	}

	@Override
	public boolean offerLast(E e) {
		if(e == null){
			throw new NullPointerException("element is null");
		}
		final ReentrantLock loc = lock;
		loc.lock();
		try{
			return linkLast(new LNode<E>(e));
		}finally{
			loc.unlock();
		}
	}

	@Override
	public boolean offerLast(E e, long timeout, TimeUnit timeUnit) throws InterruptedException {
		if(e == null){
			throw new NullPointerException("element is null");
		}
		final ReentrantLock loc = lock;
		loc.lockInterruptibly();
		try{
			LNode<E> ln = new LNode<E>(e);
			long nanos = timeUnit.toNanos(timeout);
			while(!linkLast(ln)){
				if(nanos <= 0){
					return false;
				}
				nanos = notFull.awaitNanos(nanos);
			}
			return true;
		}finally{
			loc.unlock();
		}
	}

	@Override
	public boolean offerFirst(E e) {
		if(e == null){
			throw new NullPointerException("element is null");
		}
		final ReentrantLock loc = lock;
		loc.lock();
		try{
			return linkFirst(new LNode<E>(e));
		}finally{
			loc.unlock();
		}
	}

	@Override
	public boolean offerFirst(E e, long timeout, TimeUnit timeUnit) throws InterruptedException{
		if(e == null){
			throw new NullPointerException("element is null");
		}
		long nanos = timeUnit.toNanos(timeout);
		final ReentrantLock loc = lock;
		loc.lockInterruptibly();
		try{
			LNode<E> ln = new LNode<E>(e);
			while(!linkFirst(ln)){
				if(nanos <= 0){
					return false;
				}
				nanos = notFull.awaitNanos(nanos);
			}
			return true;
		}finally{
			loc.unlock();
		}
	}

	@Override
	public E poll() {
		return pollFirst();
	}

	@Override
	public E pollFirst() {
		final ReentrantLock loc = lock;
		loc.lock();
		try{
			return unlinkFirst();
		}finally{
			loc.unlock();
		}
	}

	@Override
	public E pollFirst(long timeout, TimeUnit timeUnit) throws InterruptedException{
		long nanos = timeUnit.toNanos(timeout);
		final ReentrantLock loc = lock;
		loc.lockInterruptibly();
		try{
			E e = null;
			while((e = unlinkFirst()) != null){
				if(nanos <= 0){
					return null;
				}
				nanos = notEmpty.awaitNanos(timeout);
			}
			return e;
		}finally{
			loc.unlock();
		}
	}

	@Override
	public E pollLast() {
		final ReentrantLock loc = lock;
		loc.lock();
		try{			
			return unlinkLast();
		}finally{
			loc.unlock();
		}
	}

	@Override
	public E pollLast(long timeout, TimeUnit timeUnit) throws InterruptedException{
		long nanos = timeUnit.toNanos(timeout);
		final ReentrantLock loc = lock;
		loc.lockInterruptibly();
		try{
			E e = null;
			while((e = unlinkLast()) != null){
				if(nanos <= 0){
					return null;
				}
				nanos = notEmpty.awaitNanos(nanos);
			}
			return e;
		}finally{
			loc.unlock();
		}
	}
	
	@Override
	public boolean addFirst(E e) {
		if(!offerFirst(e)){
			throw new IllegalStateException("Deque is full");
		}
		return true;
	}
	
	@Override
	public boolean addLast(E e) {
		if(!offerLast(e)){
			throw new IllegalStateException("Deque is full");
		}
		return true;
	}

	@Override
	public boolean remove(E e) {
		if(e == null){
			return false;
		}
		final ReentrantLock loc = lock;
		loc.lock();
		try{
			for(LNode<E> f = first; f != null; f = f.next){
				if(e.equals(f.item)){
					unlink(f);
					return true;
				}
			}
			return false;
		}finally{
			loc.unlock();
		}
	}
	
	@Override
	public E take() throws InterruptedException {
		return takeFirst();
	}
	
	@Override
	public E takeFirst() throws InterruptedException {
		final ReentrantLock loc = lock;
		loc.lock();
		try{
			E e;
			while((e = unlinkFirst()) != null){
				notEmpty.await();
			}
			return e;
		}finally{
			loc.unlock();
		}
	}
	
	@Override
	public E takeLast() throws InterruptedException {
		final ReentrantLock loc = lock;
		loc.lock();
		try{
			E e;
			while((e = unlinkLast()) != null){
				notEmpty.await();
			}
			return e;
		}finally{
			loc.unlock();
		}
	}

	@Override
	public int size() {
		final ReentrantLock loc = lock;
		loc.lock();
		try{
			return count;
		}finally{
			loc.unlock();
		}
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}
	
	@Override
	public boolean hasTakeWaiters() {
		final ReentrantLock loc = lock;
		loc.lock();
		try{			
			return loc.hasWaiters(notEmpty);
		}finally{
			loc.unlock();
		}
	}
	
	@Override
	public int getTakeQueueLength() {
		final ReentrantLock loc = lock;
		loc.lock();
		try{
			return loc.getWaitQueueLength(notEmpty);
		}finally{
			loc.unlock();
		}
	}
	
	private boolean linkLast(LNode<E> ln){
		if(count >= capacity){
			return false;
		}
		final LNode<E> l = last;
		last = ln;
		ln.pre = l;
		if(l == null){
			first = ln;
		}else{
			l.next = last;
		}
		count++;
		notEmpty.signalAll();
		return true;
	}
	
	private boolean linkFirst(LNode<E> ln){
		if(count >= capacity){
			return false;
		}
		final LNode<E> oldFirst = first;
		first = ln;
		ln.next = oldFirst;
		if(oldFirst == null){
			last = ln;
		}else{
			oldFirst.pre = ln;
		}
		count++;
		notEmpty.signalAll();
		return true;
	}
	
	private E unlinkFirst(){
		if(isEmpty()){
			return null;
		}
		final LNode<E> f = first;
		final E e = f.item;
		final LNode<E> nex = f.next;
		first = nex;
		f.item = null;
		f.next = null;
		if(nex == null){
			last = null;
		}else{
			nex.pre = null;
		}
		count--;
		notFull.signalAll();
		return e;
	}
	
	private E unlinkLast(){
		if(isEmpty()){
			return null;
		}
		final LNode<E> l = last;
		final E e = l.item;
		final LNode<E> pre = l.pre;
		last = pre;
		if(pre == null){
			first = null;
		}else{
			pre.next = null;
		}
		count--;
		notFull.signalAll();
		return e;
	}
	
	private void unlink(LNode<E> p){
		if(p == null){
			return ;
		}
		final LNode<E> prev = p.pre;
		final LNode<E> nex = p.next;
		if(prev == null){
			unlinkFirst();
		}else if(nex == null){
			unlinkLast();
		}else{
			prev.next = nex;
			nex.pre = prev;
			p.item = null;
			count--;
			notFull.signalAll();
		}
	}
	
	//Deque Node=======================
	static class LNode<E>{
		E item;
		LNode<E> pre;
		LNode<E> next;
		
		LNode(E item){
			this.item = item;
		}
	}
	
	//Deque Iterator asc===============
	private class DequeAscIterator extends DequeAbstractIterator{

		@Override
		LNode<E> nextNode(LNode<E> ln) {
			return ln == null ? null : ln.next;
		}

		@Override
		LNode<E> firstNode() {
			return first;
		}
		
	}
	
	private abstract class DequeAbstractIterator implements Iterator<E>{
		
		private LNode<E> next;
		private E nextItem;
		private LNode<E> lastRet;
		
		DequeAbstractIterator(){
			final ReentrantLock loc = LLinkedBlockingDeque.this.lock;
			loc.lock();
			try{
				next = firstNode();
				nextItem = next == null ? null : next.item;
			}finally{
				loc.unlock();
			}
		}

		@Override
		public boolean hasNext() {
			return next != null;
		}

		@Override
		public E next() {
			if(next == null){
				throw new IllegalStateException("end of the deque");
			}
			E e = nextItem;
			lastRet = next;
			advance();
			return e;
		}

		private void advance() {
			final ReentrantLock loc = LLinkedBlockingDeque.this.lock;
			loc.lock();
			try{
				next = succ(next);
				nextItem = next == null ? null : next.item;
			}finally{
				loc.unlock();
			}
		}

		private LNode<E> succ(LNode<E> ln) {
			LNode<E> n = nextNode(ln);
			return n;
		}

		abstract LNode<E> nextNode(LNode<E> ln);
		
		abstract LNode<E> firstNode();

		@Override
		public void remove() {
			if(lastRet == null){
				throw new IllegalStateException("remove() must be called after next()");
			}
			final ReentrantLock loc = LLinkedBlockingDeque.this.lock;
			loc.lock();
			try{
				unlink(lastRet);
				lastRet = null;
			}finally{
				loc.unlock();
			}
		}
		
	}
	
	//Deque Iterator desc=============
	private class DequeDescendingIterator extends DequeAbstractIterator{

		@Override
		LNode<E> nextNode(LNode<E> ln) {
			return ln == null ? null : ln.pre;
		}

		@Override
		LNode<E> firstNode() {
			return last;
		}

		
	}

	@Override
	public E get(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(int index) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void linkLast(E e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public E unLinkFirst() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean add(E e) {
		// TODO Auto-generated method stub
		return false;
	}

}
