package lam.transaction.tcc.test;

import java.util.Random;

import lam.transaction.tcc.support.TransactionPointCut;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年11月29日
* @version 1.0
*/
public class MyService {
	
	private MyService1 myService1;
	
	private MyService2 myService2;
	
	@TransactionPointCut(commit = "commit0", cancel = "cancel0")
	public String doIt0(String s, int i, byte by, boolean b) {
		Random r = new Random();
		if (r.nextBoolean()) {
			throw new RuntimeException("doIt0 test throw exception");
		}
		System.out.println("doIt0");
		myService1.doIt1(s, i);
		myService2.doIt2(by, b);
		return "success";
	}
	
	private void commit0(String s, int i, byte by, boolean b) {
		System.out.println("commit0");
	}
	
	public void cancel0(String s, int i, byte by, boolean b) {
		myService1.cancel1(s, i);
		myService2.cancel2(by, b);
		System.out.println("cancel0");
	}
	
	public void setMyService1(MyService1 myService1) {
		this.myService1 = myService1;
	}
	
	public void setMyService2(MyService2 myService2) {
		this.myService2 = myService2;
	}

}
