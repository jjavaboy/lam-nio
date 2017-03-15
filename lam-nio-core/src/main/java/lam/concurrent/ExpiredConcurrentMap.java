package lam.concurrent;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
* <p>
* 过期map
* </p>
* @author linanmiao
* @date 2017年3月14日
* @versio 1.0
*/
public class ExpiredConcurrentMap <K, V>{
	
	private final ConcurrentMap<K, Entry> concurrentMap = new ConcurrentHashMap<K, Entry>();
	
	private static final long TTL_LIVE_FOREVER = -1;//never expired
	private static final long TTL_NOT_EXISTS = -2;//key not exits;
	
	public V get(K k){
		Entry entry = concurrentMap.get(k);
		return get(entry);
	}
	
	public V set(K k, V v){
		return set(new Entry(k, v));
	}
	
	public V set(K k, V v, int ttl){
		return set(new Entry(k, v, ttl));
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
		return millisTtl(entry) > 0;
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
	}

}
