package lam.design.pattern.proxy;

import lam.design.pattern.proxy.support.ProxyFactory;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年4月5日
* @version 1.0
*/
public class SubjectProxyFactory implements ProxyFactory<Subject>{

	private static volatile SubjectProxyFactory instance;
	
	public static SubjectProxyFactory getInstance(){
		if(instance == null){
			synchronized (SubjectProxyFactory.class) {
				if(instance == null){
					instance = new SubjectProxyFactory();
				}
			}
		}
		return instance;
	}

	@Override
	public Subject newProxyInstance() {
		return new ProxySubject(new RealSubject());
	}
	

}
