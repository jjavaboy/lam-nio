package lam.distribution;

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
			nodeTree.put(node.hashCode(), node);
			return true;
		}finally{
			lock.unlock();
		}
	}

	@Override
	public boolean remove(HashNode node) {
		lock.lock();
		try{
			return nodeTree.remove(node.hashCode()) != null;
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

}
