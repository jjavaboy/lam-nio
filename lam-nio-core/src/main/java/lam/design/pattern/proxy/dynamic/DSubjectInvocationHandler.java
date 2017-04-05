package lam.design.pattern.proxy.dynamic;

import java.lang.reflect.Method;

import lam.log.Console;

/**
* <p>
* dynamic
* </p>
* @author linanmiao
* @date 2017年4月5日
* @version 1.0
*/
public class DSubjectInvocationHandler implements java.lang.reflect.InvocationHandler{
	
	private Object delegate;
	
	public DSubjectInvocationHandler(Object delegate) {
		this.delegate = delegate;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		long start = System.currentTimeMillis();
		
		Object returnObject = method.invoke(delegate, args);
		
		long finish = System.currentTimeMillis();
		
		Console.println("Object:%s,method:%s,cost(ms):%d,result:%s", 
				delegate.getClass().getSimpleName(), method.getName(), finish - start, returnObject);
		
		return returnObject;
	}

}
