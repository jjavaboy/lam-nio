package lam.dubbo.loadbalance.support;
/**
* <p>
* 代理类的封装类
* </p>
* @author linanmiao
* @date 2017年5月2日
* @version 1.0
*/
public interface Invoker {
	
	public Class<?> getInvokerClass();
	
	/**
	 * get weight of invoker
	 */
	public int getWeight();
	
	/**
	 * get timestamp of invoker created
	 */
	public long getTimestamp();
	
	public Object invoke(Invocation invocation);

}
