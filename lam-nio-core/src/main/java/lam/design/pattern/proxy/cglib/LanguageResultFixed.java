package lam.design.pattern.proxy.cglib;

import lam.log.Console;
import net.sf.cglib.proxy.FixedValue;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年11月30日
* @version 1.0
*/
public class LanguageResultFixed implements FixedValue{

	@Override
	public Object loadObject() throws Exception {
		long fixedResult = 9;
		Console.println("fixed result " + fixedResult);
		return fixedResult;
	}

}
