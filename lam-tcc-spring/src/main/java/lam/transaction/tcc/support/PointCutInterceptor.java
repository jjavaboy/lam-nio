package lam.transaction.tcc.support;

import org.aspectj.lang.ProceedingJoinPoint;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年11月29日
* @version 1.0
*/
public interface PointCutInterceptor {
	
	public Object interceptAround(ProceedingJoinPoint pjp);

}
