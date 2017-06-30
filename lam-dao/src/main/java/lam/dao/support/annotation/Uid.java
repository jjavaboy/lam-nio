package lam.dao.support.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
* <p>
* unique id
* </p>
* @author linanmiao
* @date 2017年6月30日
* @version 1.0
*/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Uid {
	
	public String name() default "id";

}
