package lam.transaction.tcc;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import lam.transaction.tcc.support.PointCutInterceptor;

/**
* <p>
* transaction aspect
* </p>
* @author linanmiao
* @date 2017年11月29日
* @version 1.0
*/
@Aspect
public class TransactionAspect {
	
	private PointCutInterceptor pointCutInterceptor;
	
	@Pointcut("@annotation(lam.transaction.tcc.support.TransactionPointCut)")
	public void pointCut() {
	    //do nothing.	
	}
	
	@Around("pointCut()")
	public Object around(ProceedingJoinPoint pjp) {
		return pointCutInterceptor.interceptAround(pjp);
	}
	
	public void setPointCutInterceptor(PointCutInterceptor pointCutInterceptor) {
		this.pointCutInterceptor = pointCutInterceptor;
	}

}
