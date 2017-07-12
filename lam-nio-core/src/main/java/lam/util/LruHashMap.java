package lam.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
* <p>
* LRU hashmap
* </p>
* @author linanmiao
* @date 2017年7月12日
* @version 1.0
*/
public class LruHashMap<K, V> implements Map<K, V>, Serializable{

	private static final long serialVersionUID = 1496214921791891782L;
	
	private final int capicity;
	private HashMap<K, Entry<K, V>> nodes;
	/**
	 * header.after:tail of linked list(the last element)
	 * header.before:head of linked list(the first element)
	 */
	protected Entry<K, V> header;
	
	public LruHashMap(int capicity){
		this.capicity = capicity;
		this.nodes = new HashMap<K, Entry<K, V>>(this.capicity, 0.75f);
		init();
	}
	
	protected void init(){
		header = new Entry<K, V>(null, null);
		header.before = header.after = header;
	}
	
	protected boolean removeEldestEntry(){
		return size() > capicity;
	}

	@Override
	public int size() {
		return nodes.size();
	}

	@Override
	public boolean isEmpty() {
		return nodes.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return nodes.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return nodes.containsValue(value);
	}

	@Override
	public V get(Object key) {
		Entry<K, V> entry = nodes.get(key);
		return entry == null ? null : entry.getValue();
	}

	@Override
	public V put(K key, V value) {
		Entry<K, V> entry = nodes.get(key);
		if(entry == null){
			entry = new Entry<K, V>(key, value);
			entry.addBefore(header);
		}else
			entry.setValue(value);
		Entry<K, V> oldEntry = nodes.put(key, entry);
		if(removeEldestEntry()){
			Entry<K, V> eldestEntry = header.after;
			eldestEntry.remove();
			nodes.remove(eldestEntry.getKey());
		}
		if(oldEntry != null){
			return oldEntry.getValue();
		}
		return null;
	}

	@Override
	public V remove(Object key) {
		Entry<K, V> oldEntry = nodes.remove(key);
		if(oldEntry != null){
			oldEntry.remove();
			return oldEntry.getValue();
		}
		return null;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		
	}

	@Override
	public void clear() {
		//clear nodes and clear linked list
		nodes.clear();
		header.after = header.before = header;
	}

	@Override
	public Set<K> keySet() {
		return nodes.keySet();
	}

	@Override
	public Collection<V> values() {
		//Collection<Entry<K, V>> collection = nodes.values();
		return null;
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		//return nodes.entrySet();
		return null;
	}
	
	@Override
	public String toString(){
		return nodes.toString();
	}
	
	private static class Entry<K, V> implements Map.Entry<K, V>{
		final K key;
		V value;
		Entry<K, V> before;
		Entry<K, V> after;
		
		Entry(K key, V value){
			this.key = key;
			this.value = value;
		}

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}

		@Override
		public V setValue(V value) {
			V oldValue = this.value;
			this.value = value;
			return oldValue;
		}
		
		/**
		 * remove this entry itself from linked list.
		 */
		void remove(){
			this.after.before = this.before;
			this.before.after = this.after;
		}
		
		/**
		 * add this entry to be before of existingEntry
		 * @param existingEntry
		 */
		void addBefore(Entry<K, V> existingEntry){
			after = existingEntry;			
			before = existingEntry.before;
			before.after = this;
			existingEntry.before = this;
		}
		
		@Override
		public String toString() {
			return new StringBuilder("{").append(getKey()).append("=").append(getValue()).append("}").toString();
		}
	}

}
