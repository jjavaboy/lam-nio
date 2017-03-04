package lam.dubbo.extension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年3月4日
* @versio 1.0
*/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DubboAdaptive {

	String[] value() default {};
	
}
