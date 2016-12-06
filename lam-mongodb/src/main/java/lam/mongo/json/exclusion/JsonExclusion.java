package lam.mongo.json.exclusion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
* <p>
* json中排除的属性<br/>
* Retention=RetentionPolicy.RUNTIME:注解在运行时保留
* Target=ElementType.FIELD:注解作用在类的属性上
* </p>
* @author linanmiao
* @date 2016年12月6日
* @version 1.0
*/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface JsonExclusion {

}
