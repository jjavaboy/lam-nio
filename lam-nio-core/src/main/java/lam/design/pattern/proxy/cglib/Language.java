package lam.design.pattern.proxy.cglib;

import lam.log.Console;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年11月30日
* @version 1.0
*/
public class Language {
	
	public String c(String arg) {
		Console.println(arg);
		return arg;
	}
	
	public int cPlus(int arg) {
		Console.println(arg);
		return arg;
	}
	
	public long java(long arg) {
		Console.println(arg);
		return arg;
	}

}
