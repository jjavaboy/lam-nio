package lam.design.pattern.chainofresponsibility.dubbo;
/**
* <p>
* invoker
* </p>
* @author linanmiao
* @date 2017年7月6日
* @version 1.0
*/
public interface Invoker {

	public Object invoke(Object param) throws Exception;
	
}
