package lam.transaction.tcc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;

import lam.transaction.tcc.model.TransactionTarget;
import lam.transaction.tcc.support.TransactionHandler;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年11月29日
* @version 1.0
*/
public class TransactionHandlerImpl implements TransactionHandler{

	@Override
	public void begin() {
	}
	
	@Override
	public Object tryIt(ProceedingJoinPoint pjp, TransactionTarget transactionTarget) {
		try {
			return pjp.proceed();
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void commit(ProceedingJoinPoint pjp, TransactionTarget transactionTarget) {
		Method[] methods = transactionTarget.getTarget().getClass().getDeclaredMethods();
		for (Method method : methods) {
			if (method.getName().equals(transactionTarget.getMethod())) {
				if (!method.isAccessible()) {
					method.setAccessible(Boolean.TRUE.booleanValue());
				}
				try {
					method.invoke(transactionTarget.getTarget(), transactionTarget.getArgs());
					return ;
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				} catch (IllegalArgumentException e) {
					throw new RuntimeException(e);
				} catch (InvocationTargetException e) {
					throw new RuntimeException(e);
				}
			}
		}
		throw new RuntimeException("class " + transactionTarget.getTarget().getClass()
				+ " do not contains commit method: " + transactionTarget.getMethod() + ".");
	}

	@Override
	public void cancel(ProceedingJoinPoint pjp, TransactionTarget transactionTarget) {
		Method[] methods = transactionTarget.getTarget().getClass().getDeclaredMethods();
		for (Method method : methods) {
			if (method.getName().equals(transactionTarget.getMethod())) {
				if (!method.isAccessible()) {
					method.setAccessible(Boolean.TRUE.booleanValue());
				}
				try {
					method.invoke(transactionTarget.getTarget(), transactionTarget.getArgs());
					return ;
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				} catch (IllegalArgumentException e) {
					throw new RuntimeException(e);
				} catch (InvocationTargetException e) {
					throw new RuntimeException(e);
				}
			}
		}
		throw new RuntimeException("class " + transactionTarget.getTarget().getClass()
				+ " do not contains cancel method: " + transactionTarget.getMethod() + ".");
	}

	@Override
	public void finish() {
	}
}
