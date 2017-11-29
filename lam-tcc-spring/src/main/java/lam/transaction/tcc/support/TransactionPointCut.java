package lam.transaction.tcc.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
* <p>
* It will throws CanceledException when tcc transaction executes try or commit method failed.
* </p>
* @author linanmiao
* @date 2017年11月29日
* @version 1.0
*/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface TransactionPointCut {
	
	//public String tryIt() default "";
	
	/**
	 * commit method should satisfy idempotency.
	 */
	public String commit() default "";
	
	/**
	 * cancel method should satisfy idempotency.
	 */
	public String cancel() default "";

}
