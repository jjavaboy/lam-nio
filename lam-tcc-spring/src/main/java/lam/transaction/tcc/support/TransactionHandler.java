package lam.transaction.tcc.support;

import org.aspectj.lang.ProceedingJoinPoint;

import lam.transaction.tcc.model.TransactionTarget;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年11月29日
* @version 1.0
*/
public interface TransactionHandler {
	
	public void begin();
	
	public Object tryIt(ProceedingJoinPoint pjp, TransactionTarget transactionTarget);
	
	public void commit(ProceedingJoinPoint pjp, TransactionTarget transactionTarget);
	
	public void cancel(ProceedingJoinPoint pjp, TransactionTarget transactionTarget);
	
	public void finish();

}
