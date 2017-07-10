package lam.util;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
* <p>
* least recently used cache, initialized with a special capicity.
* </p>
* @author linanmiao
* @date 2017年7月10日
* @version 1.0
*/
public class LRUCache <K, V> implements Map<K, V>{
	
	private int capicity;
	
	private LinkedHashMap<K, V> cache;
	
	public LRUCache(int capicity){
		this.capicity = capicity;
		this.cache = new LinkedHashMap<K, V>(this.capicity, 0.75f, true){

			private static final long serialVersionUID = 1L;
			
			@Override
			protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
				return super.size() > LRUCache.this.capicity;
			}
		};
	}

	@Override
	public int size() {
		return cache.size();
	}

	@Override
	public boolean isEmpty() {
		return cache.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return cache.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return cache.containsValue(value);
	}

	@Override
	public V get(Object key) {
		return cache.get(key);
	}

	@Override
	public V put(K key, V value) {
		return cache.put(key, value);
	}

	@Override
	public V remove(Object key) {
		return cache.remove(key);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		cache.putAll(m);
	}

	@Override
	public void clear() {
		cache.clear();
	}

	@Override
	public Set<K> keySet() {
		return cache.keySet();
	}

	@Override
	public Collection<V> values() {
		return cache.values();
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return cache.entrySet();
	}
	
	@Override
	public String toString() {
		return cache.toString();
	}

}
