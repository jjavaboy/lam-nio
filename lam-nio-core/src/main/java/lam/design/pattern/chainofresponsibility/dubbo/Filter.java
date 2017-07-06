package lam.design.pattern.chainofresponsibility.dubbo;
/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年7月6日
* @version 1.0
*/
public interface Filter {
	
	public Object invoke(Invoker invoker, Object object)throws Exception;

}
