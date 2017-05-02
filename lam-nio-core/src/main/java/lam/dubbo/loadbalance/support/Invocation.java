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
	
	public String getMethodName();
	
	public Class<?>[] getParameterTypes();
	
	public Object[] getParameters();

}
