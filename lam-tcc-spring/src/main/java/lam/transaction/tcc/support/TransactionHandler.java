package lam.transaction.tcc.support;

import org.aspectj.lang.ProceedingJoinPoint;

import lam.transaction.tcc.model.TransactionTarget;

/**
* <p>
* To define the process of tcc.
* </p>
* @author linanmiao
* @date 2017年11月29日
* @version 1.0
*/
public interface TransactionHandler {
	/**
	 * Begin method of tcc.
	 */
	public void begin();
	
	public Object tryIt(ProceedingJoinPoint pjp, TransactionTarget transactionTarget);
	
	/**
	 * commit method should satisfy idempotency.
	 */
	public void commit(ProceedingJoinPoint pjp, TransactionTarget transactionTarget);
	
	/**
	 * cancel method should satisfy idempotency.
	 */
	public void cancel(ProceedingJoinPoint pjp, TransactionTarget transactionTarget);
	
	/**
	 * Finish method of tcc, even occurs exception.
	 */
	public void finish();

}
