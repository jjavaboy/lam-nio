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
public class MyService2 {
	
	@TransactionPointCut(commit = "commit2", cancel = "cancel2")
	public String doIt2(byte by, boolean b) {
		Random r = new Random();
		if (r.nextBoolean()) {
			throw new RuntimeException("doIt2 test throw exception");
		}
		System.out.println("doIt2");
		return "success";
	}
	
	public void commit2(byte by, boolean b) {
		System.out.println("commit2");
	}
	
	public void cancel2(byte by, boolean b) {
		System.out.println("cancel2");
	}

}
