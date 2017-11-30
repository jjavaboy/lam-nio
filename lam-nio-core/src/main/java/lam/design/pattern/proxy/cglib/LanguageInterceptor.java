package lam.design.pattern.proxy.cglib;

import java.lang.reflect.Method;

import lam.log.Console;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年11月30日
* @version 1.0
*/
public class LanguageInterceptor implements MethodInterceptor{

	@Override
	public Object intercept(Object target, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		Console.println("before");
		Object result = proxy.invokeSuper(target, args);
		Console.println("after");
		return result;
	}

}
