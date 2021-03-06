package lam.design.pattern.proxy.dynamic;

import lam.design.pattern.proxy.support.ProxyFactory;

/**
* <p>
* dynamic
* </p>
* @author linanmiao
* @date 2017年4月5日
* @version 1.0
*/
public class DSubjectProxyFactory implements ProxyFactory<DSubject>{
	
	private static class InstanceHolder{
		private static final DSubjectProxyFactory INSTANCE = new DSubjectProxyFactory();
	}
	
	//Constructor can not be called at the outside.
	private DSubjectProxyFactory(){}
	
	public static DSubjectProxyFactory getInstance(){
		return InstanceHolder.INSTANCE;
	}

	@Override
	public DSubject newProxyInstance() {
		
		DSubject delegate = new DRealSubject();
		java.lang.reflect.InvocationHandler invocationHandler = new DSubjectInvocationHandler(delegate);
		DSubject proxy = (DSubject) java.lang.reflect.Proxy.newProxyInstance(
				delegate.getClass().getClassLoader(),
				delegate.getClass().getInterfaces(), 
				invocationHandler);

		//delegate.getClass().getClassLoader() ==>> sun.misc.Launcher$AppClassLoader
		
		//delegate.getClass().getInterfaces() ==>> [lam.design.pattern.proxy.dynamic.DSubject]
		//because DRealSubject class implements the lam.design.pattern.proxy.dynamic.DSubject interface
		
		return proxy;
	}
	
}
