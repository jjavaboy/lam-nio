package lam.concurrent;

import java.io.Closeable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
* <p>
* 过期map<br/>
* 两个方法:<br/>
* 1.操作key时操作判断是否过期<br/>
* 2.定期判断key是否过期，每次定期判断20个<br/>
* </p>
* @author linanmiao
* @date 2017年3月14日
* @versio 1.0
*/
public class ExpiredConcurrentMap <K, V> implements Closeable{
	
	private final ConcurrentMap<K, Entry> concurrentMap = new ConcurrentHashMap<K, Entry>();
	private final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = 
			new ScheduledThreadPoolExecutor(1, new ExpiredThreadFactory(), new ExpiredRejectExecutorHandler());
	private final int expiredKeyNumberEachTask = 20;
	
	private static final long TTL_LIVE_FOREVER = -1;//never expired
	private static final long TTL_NOT_EXISTS = -2;//key not exits;
	
	public ExpiredConcurrentMap(){
		startSchedule();
	}
	
	/**
	 * start the schedule to determine if the key is expired.
	 */
	private void startSchedule(){
		scheduledThreadPoolExecutor.scheduleAtFixedRate(new Runnable(){
			@Override
			public void run() {
				Thread t = Thread.currentThread();
				System.out.print("[" + t.getName() + "]schedule to check key expired start.\n=============================================\n");
				Iterator<Map.Entry<K, Entry>> iter = concurrentMap.entrySet().iterator();
				int expiredKeyNumber = 0;
				while(iter.hasNext()){
					Map.Entry<K, Entry> entry = iter.next();
					if(entry.getValue() != null && isExpired(entry.getValue())){
						//remove the key when it is expired and increment expired number
						remove(entry.getKey());
						expiredKeyNumber++;
						System.out.println("[" + t.getName() + "]entry expired:" + entry.getValue());
					}
					if(expiredKeyNumber >= expiredKeyNumberEachTask){
						break ;
					}
				}
			}
		}, 5000, 10000, TimeUnit.MILLISECONDS);
	}
	
	public V get(K k){
		Entry entry = concurrentMap.get(k);
		return get(entry);
	}
	
	public V set(K k, V v){
		Entry entry = concurrentMap.get(k);
		if(entry == null){
			entry = new Entry(k, v);
		}else{
			entry.v = v;			
		}
		return set(entry);
	}
	
	public V set(K k, V v, long ttl){
		Entry entry = concurrentMap.get(k);
		if(entry == null){
			entry = new Entry(k, v, ttl); 
		}else{
			entry.v = v;
			entry.setTtl(ttl);
		}
		return set(entry);
	}
	
	/**
	 * @param ttl, millisecond
	 */
	public boolean setTtl(K k, long ttl){
		Entry entry = concurrentMap.get(k);
		if(entry == null){
			return false;
		}
		entry.setTtl(ttl);
		set(entry);
		return true;
	}
	
	public V remove(K k){
		Entry entry = concurrentMap.remove(k);
		return entry == null ? null : entry.v;
	}
	
	public long ttl(K k){
		Entry entry = concurrentMap.get(k);
		return millisTtl(entry);
	}
	
	private long millisTtl(Entry entry){		
		if(entry == null){
			return TTL_NOT_EXISTS;
		}
		if(isLiveForever(entry)){
			return TTL_LIVE_FOREVER;
		}
		Date date = new Date(entry.initMillisecond + entry.getTtl());
		Date nowDate = new Date();
		if(date.after(nowDate)){
			return date.getTime() - nowDate.getTime();
		}
		remove(entry.k);
		return TTL_NOT_EXISTS;
	}
	
	private V set(Entry entry){
		Entry oldEntry = concurrentMap.put(entry.k, entry);
		return oldEntry == null ? null : oldEntry.v;
	}
	
	private boolean isExpired(Entry entry){
		checkNull(entry);
		return millisTtl(entry) == TTL_NOT_EXISTS;
	}
	
	private boolean isLiveForever(Entry entry){
		checkNull(entry);
		return entry.getTtl() == TTL_LIVE_FOREVER;
	}
	
	private V get(Entry entry){
		if(entry == null){
			return null;
		}
		return isExpired(entry) ? null : entry.v;
	}
	
	private void checkNull(Entry entry){
		if(entry == null){
			throw new NullPointerException("entry is null");
		}
	}
	
	final class Entry{
		final K k;
		volatile V v;
		private AtomicLong ttl = new AtomicLong(TTL_LIVE_FOREVER);//time to live, millisecond.
		private long initMillisecond = System.currentTimeMillis();
		Entry(K k, V v){
			this.k = k;
			this.v = v;
		}
		
		Entry(K k, V v, long ttl){
			this(k, v);
			setTtl(ttl);
		}
		
		public void setTtl(long ttl) {
			checkTtl(ttl);
			this.initMillisecond = System.currentTimeMillis();
			this.ttl.getAndSet(ttl);
		}
		
		public long getTtl() {
			return ttl.get();
		}
		
		private void checkTtl(long ttl){
			if(ttl <= 0){
				throw new IllegalArgumentException("ttl must positive");
			}
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("date:").append(getCurrentTime(new Date(initMillisecond + getTtl()), "yyyy-MM-hh HH:mm:ss SSS"))
			.append("{").append("K:").append(k).append(", V:").append(v).append(", ttl:").append(getTtl())
			.append(", initMillisecond:").append(initMillisecond).append("}");
			return sb.toString();
		}
	}
	
	static class ExpiredThreadFactory implements ThreadFactory{
		private static final AtomicInteger THREAD_POOL_NUMBER = new AtomicInteger(0);
		private final AtomicInteger threadNumber = new AtomicInteger(0);
		private ThreadGroup threadGroup;
		private String namePrefix;
		
		ExpiredThreadFactory(){
			SecurityManager securityManager = System.getSecurityManager();
			threadGroup = securityManager != null ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
			
			namePrefix = String.format("ExpiredThreadPool-%d-thread-", THREAD_POOL_NUMBER.incrementAndGet());
		}
		
		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(threadGroup, r, namePrefix + threadNumber.incrementAndGet(), 0);
			if(t.isDaemon()){
				t.setDaemon(false);
			}
			if(t.getPriority() != Thread.NORM_PRIORITY){
				t.setPriority(Thread.NORM_PRIORITY);
			}
			return t;
		}
		
	}
	
	private class ExpiredRejectExecutorHandler implements RejectedExecutionHandler{

		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			System.out.println(String.format("Runnable(%s) reject from ThreadPoolExecutor(%s)", r, executor));
		}
		
	}

	@Override
	public void close(){
		concurrentMap.clear();
		scheduledThreadPoolExecutor.shutdown();
	}
	
	public static void main(String[] args) {
		java.io.PrintStream out = new java.io.PrintStream(System.out){
			@Override
			public void println(String x) {
				x = getCurrentTime(new Date(), "yyyy-MM-dd HH:mm:ss SSS") + ":" + x;
				super.println(x);
			}
		};
		System.setOut(out);
		
		ExpiredConcurrentMap<String, String> expiredMap = new ExpiredConcurrentMap<>();
		Random r = new Random(); 
		for(int i = 0; i < 1000; i++){
			expiredMap.set("key" + i, "value" + i, 10 * 000 + r.nextInt(6 * 100 * 1000));
		}
		
		synchronized (ExpiredConcurrentMap.class) {
			try {
				ExpiredConcurrentMap.class.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		expiredMap.close();
	}
	
	public static String getCurrentTime(Date date, String format){
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}

}
