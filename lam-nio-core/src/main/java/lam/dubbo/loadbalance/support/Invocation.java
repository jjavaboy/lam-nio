package lam.dubbo.loadbalance.support;
/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年5月2日
* @versio 1.0
*/
public interface Invocation {
	
	public Class<?> getInterface();
	
	public String getMethodName();
	
	public Class<?>[] getParameterTypes();
	
	public Object[] getParameters();
	
	/**
	 * retry times of invoke the service, excluding the first time.
	 * @return default value is 2
	 */
	public int getRetryTimes();

}
