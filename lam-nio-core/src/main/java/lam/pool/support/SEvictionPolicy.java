package lam.pool.support;
/**
* <p>
* evict policy
* </p>
* @author linanmiao
* @date 2017年3月29日
* @versio 1.0
*/
public interface SEvictionPolicy<SPooledObject>{

	/**
	 * determine T p is need to be evicted.
	 * @param p
	 * @return true/false
	 */
	boolean evict(SPooledObject p);
	
}
