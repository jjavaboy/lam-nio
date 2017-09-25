package lam.distribution;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
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
	private /*SortedMap*/TreeMap<Integer, HashNode> nodeTree = new TreeMap<Integer, HashNode>();
	
	private Lock lock  = new ReentrantLock();
	
	public ConsistentHash(HashNode... nodes){
		for(HashNode node : nodes){
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
			int hashCode = md5Hash.hash(node.toString());
			nodeTree.put(hashCode, node);
			return true;
		}finally{
			lock.unlock();
		}
	}

	@Override
	public boolean remove(HashNode node) {
		lock.lock();
		try{
			int hashCode = md5Hash.hash(node.toString());
			return nodeTree.remove(hashCode) != null;
		}finally{
			lock.unlock();
		}
	}
	
	private HashNode route(int hashCode){
		Map.Entry<Integer, HashNode> node = nodeTree.higherEntry(hashCode);
		if(node == null){
			lock.lock();
			try{
				node = nodeTree.higherEntry(hashCode);
				if(node == null){
					node = nodeTree.firstEntry();
				}
			}finally{
				lock.unlock();
			}
		}
		return node.getValue();
	}
	
	private static class MD5Hash{
		MessageDigest instance;

		public MD5Hash() {
			try {
				instance = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
			}
		}
		
		int hash(String key) {
			instance.reset();
			instance.update(key.getBytes());
			byte[] digest = instance.digest();

			int h = 0;
			for (int i = 0; i < 4; i++) {
				h <<= 8;
				h |= ((int) digest[i]) & 0xFF;
			}
			return h;
		}
	}

}
