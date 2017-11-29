package lam.transaction.tcc;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import lam.transaction.tcc.support.TransactionPointCut;
import lam.transaction.tcc.exception.CanceledException;
import lam.transaction.tcc.model.TransactionTarget;
import lam.transaction.tcc.support.PointCutInterceptor;
import lam.transaction.tcc.support.TransactionHandler;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年11月29日
* @version 1.0
*/
public class TransactionPointCutInterceptor implements PointCutInterceptor{
	
	private TransactionHandler transactionHandler;

	@Override
	public Object interceptAround(ProceedingJoinPoint pjp) {
		MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
		Method method = methodSignature.getMethod();
		
		TransactionPointCut transactionPointCut = method.getAnnotation(TransactionPointCut.class);
		
		String commit = transactionPointCut.commit();
		String cancel = transactionPointCut.cancel();
		Object target = pjp.getTarget();
		
		Object obj = null;
		
		TransactionTarget transactionTarget = new TransactionTarget(target);
		
		transactionHandler.begin();
		
		try {
			//try
			obj = transactionHandler.tryIt(pjp, transactionTarget);
			
			//commit
			transactionHandler.commit(pjp, transactionTarget.setMethod(commit).setArgs(pjp.getArgs()));
		} catch (Throwable t) {
			//cancel
			transactionHandler.cancel(pjp, transactionTarget.setMethod(cancel).setArgs(pjp.getArgs()));
			
			throw new CanceledException("tcc transaction has canceled.", t);
		} finally {
			transactionHandler.finish();
		}
		
		return obj;
	}
	
	public void setTransactionHandler(TransactionHandler transactionHandler) {
		this.transactionHandler = transactionHandler;
	}

}
