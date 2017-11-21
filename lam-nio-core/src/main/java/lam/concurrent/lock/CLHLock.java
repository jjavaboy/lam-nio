package lam.concurrent.lock;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import lam.concurrent.lock.support.LLock;
import lam.log.Console;
import lam.util.Threads;
import lam.util.concurrent.ThreadFactoryBuilder;

/**
* <p>
* CLH:Craig，Landin and Hagersten
* </p>
* <p>
* 参考: 
* http://blog.csdn.net/aesop_wubo/article/details/7533186<br/>
* http://blog.csdn.net/chenssy/article/details/50432195<br/>
* </p>
* @author linanmiao
* @date 2017年11月20日
* @version 1.0
*/
public class CLHLock implements LLock{

	AtomicReference<QNode> tail;
	
	ThreadLocal<QNode> myPred;
	
	ThreadLocal<QNode> myNode;
	
	public CLHLock() {
		tail = new AtomicReference<QNode>(new QNode(false)); //创建的默认队尾结点，默认是释放锁的状态（locked为false）
		myNode = new ThreadLocal<QNode>(){
			protected QNode initialValue() {
				return new QNode();
			}
		};
		myPred = new ThreadLocal<QNode>(){
			protected QNode initialValue() {
				return null;
			}
		};
	}
	
	@Override
	public void lock() {
		QNode qnode = myNode.get(); //当前线程创建一个节点QNode
		qnode.locked = Boolean.TRUE.booleanValue(); //当前线程需要获得锁
		QNode pred = tail.getAndSet(qnode); //取到前趋节点QNode pred，然后设置当前线程的节点到队尾
		myPred.set(pred); //设置当前线程的前趋节点QNode pred
		Console.println("before myNode:%s, myPred:%s", qnode, pred);
		while (pred.locked){} //一直循环直到前趋节点pred释放锁（pred.locked为false）
		Console.println("after myNode:%s, myPred:%s", qnode, pred);
	}

	@Override
	public void unlock() {
		QNode qnode = myNode.get(); //当前线程锁的QNode节点
		qnode.locked = Boolean.FALSE.booleanValue(); //设置当前线程释放锁的状态
		Console.println("myNode:%s, myPred:%s", qnode, myPred.get());
		//myNode.set(myPred.get());
		myNode.remove(); //解除myNode绑定在当前线程上，即使下次当前线程再次需要获得锁时则再创建一个QNode。
		myPred.remove();
	}
	
	private static class QNode{
		final int id;
		
		volatile boolean locked;
		
		final String name = Thread.currentThread().getName() + "-QNode";
		
		private final static AtomicInteger idCount = new AtomicInteger(0);
		
		QNode(){
			this.id = idCount.getAndIncrement();
		}
		
		QNode(boolean locked) {
			this();
			this.locked = locked;
		}

		@Override
		public String toString() {
			return "QNode{id:" + id + ", locked:" + locked + ", name:" + name + "}";
		}
		
	}
	
	//test below=================================
	public static void main(String[] args) {
		System.setProperty("lam.log.isAsyn", Boolean.TRUE.toString());
		ThreadPoolExecutor executor = new ThreadPoolExecutor(
				20, 
				40, 
				0, 
				TimeUnit.MILLISECONDS, 
				new LinkedBlockingQueue<Runnable>(),
				new ThreadFactoryBuilder().setThreadNamePrefix("TestCLHLock").build(),
				new ThreadPoolExecutor.DiscardPolicy());
		final Counter counter = new Counter();
		final Random r = new Random();
		for (int i = 0; i < executor.getMaximumPoolSize(); i++) {
			executor.execute(new Runnable(){
				public void run() {
					Threads.sleepWithUninterrupt(r.nextInt(10));
					Console.println(counter.incrementAndGet());
				};
			});
		}
		
	}
	
	private static class Counter {
		private volatile int count;
		
		LLock lock = new CLHLock();
		
		public int incrementAndGet(){
			lock.lock();
			try{
				count++;
				return count;
			} finally {
				lock.unlock();
			}
		}
	}
	
}
