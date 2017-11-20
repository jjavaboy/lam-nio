package lam.concurrent.lock;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
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
		QNode pred = tail.getAndSet(qnode); //当前线程的节点设置到队尾，取到前趋节点QNode pred
		myPred.set(pred);
		while (pred.locked) ;
	}

	@Override
	public void unlock() {
		QNode qnode = myNode.get();
		qnode.locked = Boolean.FALSE.booleanValue();
		myNode.set(myPred.get());
	}
	
	private class QNode{
		volatile boolean locked;
		
		QNode(){}
		
		QNode(boolean locked) {
			this.locked = locked;
		}
	}
	
	public static void main(String[] args) {
		ThreadPoolExecutor executor = new ThreadPoolExecutor(
				20, 
				20, 
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
					Threads.sleepWithUninterrupt(r.nextInt(300));
					Console.println(counter.incrementAndGet());
				};
			});
		}
	}
	
	private static class Counter {
		private int count;
		
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
