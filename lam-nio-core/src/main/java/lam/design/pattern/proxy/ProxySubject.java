package lam.design.pattern.proxy;

import lam.log.Console;

/**
* <p>
* proxy subject
* </p>
* @author linanmiao
* @date 2017年4月5日
* @version 1.0
*/
public class ProxySubject implements Subject{

	private Subject subject;
	
	public ProxySubject(Subject subject){
		this.subject = subject;
	}
	
	@Override
	public boolean doWork(String argument) {
		long start = System.currentTimeMillis();
		boolean result = subject.doWork(argument);
		long finish = System.currentTimeMillis();
		Console.println("subject:%s,method:%s,argument:%s,cost(ms):%d,result:%b", 
				subject.getClass().getSimpleName(), "doWork", argument, finish - start, result);
		return result;
	}

}
