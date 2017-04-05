package lam.design.pattern.proxy.support;
/**
* <p>
* factorty to create proxy
* </p>
* @author linanmiao
* @date 2017年4月5日
* @version 1.0
*/
public interface ProxyFactory <T>{
	
	public T newProxyInstance();

}
