package lam.design.pattern.proxy.cglib;

import lam.log.Console;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年11月30日
* @version 1.0
*/
public class ProxyMain {
	
	public static void main(String[] args) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(Language.class);
		enhancer.setCallback(new LanguageInterceptor());
		Language language = (Language) enhancer.create();
		Console.println(language.c("aabb"));
		Console.println(language.cPlus(2));
		Console.println(language.java(3L));
		
		System.out.println("");
		
		CallbackFilter callbackFilter = new LanguageMethodCallbackFilter();
		
		Callback noopCb = NoOp.INSTANCE; //NoOp.INSTANCE：这个NoOp表示no operator，即什么操作也不做，代理类直接调用被代理的方法不进行拦截。
		Callback callback = new LanguageInterceptor();
		Callback fixedValue = new LanguageResultFixed();
		
		Callback[] callbacks = new Callback[]{noopCb, callback, fixedValue};
		
		Enhancer enhancer1 = new Enhancer();
		enhancer1.setSuperclass(Language.class);
		enhancer1.setCallbacks(callbacks);
		enhancer1.setCallbackFilter(callbackFilter);
		
		Language language1 = (Language) enhancer1.create();
		Console.println(language1.c("ddee"));
		Console.println(language1.cPlus(4));
		Console.println(language1.java(7L));
		
	}

}
