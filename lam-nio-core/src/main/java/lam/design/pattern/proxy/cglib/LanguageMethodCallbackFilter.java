package lam.design.pattern.proxy.cglib;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.CallbackFilter;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年11月30日
* @version 1.0
*/
public class LanguageMethodCallbackFilter implements CallbackFilter{

	@Override
	public int accept(Method method) {
		switch (method.getName()) {
			case "c" :
				return 0;
			case "cPlus" :
				return 1;
			case "java" :
				return 2;
		}
		return 0;
	}

}
