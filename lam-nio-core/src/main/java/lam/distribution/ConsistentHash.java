package lam.distribution;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
* <p>
* 一致性哈希
* </p>
* @author linanmiao
* @date 2017年9月22日
* @version 1.0
*/
public class ConsistentHash implements Hashing{
	
	private MD5Hash md5Hash = new MD5Hash();
	
	/**
	 * key实现java.lang.Comparable，或者构造函数传参java.lang.Comparable。<br/>
	 * nodeTree will be guarded by lock
	 */
	private /*SortedMap*/TreeMap<Long, HashNode> nodeTree = new TreeMap<Long, HashNode>();
	
	private Lock lock  = new ReentrantLock();
	
	//dubbo loadbalance module code: this.replicaNumber = url.getMethodParameter(methodName, "hash.nodes", 160);
	//or jedis code: redis.clients.util.Sharded.initialize(List<S>), replicaNumber is also 160. 
	private int replicaNumber; /*  = 160 */ 
	
	public ConsistentHash(HashNode... nodes){
		//default value: 160
		this(160, nodes);
	}
	
	public ConsistentHash(int replicaNumber, HashNode... nodes){
		this.replicaNumber = replicaNumber;
		for (HashNode node : nodes) {
			add(node);
		}
	}

	@Override
	public HashNode select(Object obj) {
		return route(obj.hashCode());
	}

	@Override
	public boolean add(HashNode node) {
		lock.lock();
		try{
			for (int i = 0; i < replicaNumber; i++) {
				long hashCode = md5Hash.hash(node.toString() + i);
				nodeTree.put(hashCode, node);
			}
			return true;
		}finally{
			lock.unlock();
		}
	}

	@Override
	public boolean remove(HashNode node) {
		lock.lock();
		try{
			for (int i = 0; i < replicaNumber; i++) {
				long hashCode = md5Hash.hash(node.toString() + i);
				nodeTree.remove(hashCode);
			}
			return true;
		}finally{
			lock.unlock();
		}
	}
	
	private HashNode route(long hashCode){
		if (nodeTree.containsKey(hashCode)) {
			return nodeTree.get(hashCode);
		}
		long key;
		//TreeMap.tailMap(Long fromKey): Returns a view of the portion of this map whose keys are greater than or equal to fromKey.
		SortedMap<Long, HashNode> greaterMap = nodeTree.tailMap(hashCode);
		if (greaterMap.isEmpty()) {
			key = nodeTree.firstKey();
		} else {
			key = greaterMap.firstKey();
		}
		return nodeTree.get(key);
	}
	
	private static class MD5Hash{
		MessageDigest instance;

		public MD5Hash() {
			try {
				instance = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
			}
		}
		
		//look for: redis.clients.util.Hashing.MD5.hash(byte[])
		long hash(String key) {
			instance.reset();
			instance.update(key.getBytes());
			byte[] digest = instance.digest();
			
			long h = 0;
			h = ((long)(digest[3] & 0xFF) << 24) |
				((long)(digest[2] & 0xFF) << 16) |
				((long)(digest[1] & 0xFF) << 8)  |
				((long)(digest[0] & 0xFF));
			
			return h;
		}
	}

}
