package lam.distribution;
/**
* <p>
* 一致性哈希的接口
* </p>
* @author linanmiao
* @date 2017年9月22日
* @version 1.0
*/
public interface Hashing {
	/**
	 * 根据对象obj的哈希值找到对应的节点
	 * @param obj 对象
	 * @return HashNode 节点
	 */
	public HashNode select(Object obj);
	
	/**
	 * 添加一个HashNode节点
	 * @param node
	 * @return
	 */
	public boolean add(HashNode node);
	
	/**
	 * 删除一个HashNode节点
	 * @param node
	 * @return
	 */
	public boolean remove(HashNode node);

}
