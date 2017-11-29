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
public class MyService1 {
	
	@TransactionPointCut(commit = "commit1", cancel = "cancel1")
	public String doIt1(String s, int i) {
		Random r = new Random();
		if (r.nextBoolean()) {
			throw new RuntimeException("doIt1 test throw exception");
		}
		System.out.println("doIt1");
		return "success";
	}
	
	public void commit1(String s, int i) {
		System.out.println("commit1");
	}
	
	public void cancel1(String s, int i) {
		System.out.println("cancel1");
	}

}
