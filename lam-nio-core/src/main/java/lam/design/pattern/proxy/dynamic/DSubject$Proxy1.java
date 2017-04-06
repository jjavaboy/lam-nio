package lam.design.pattern.proxy.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;

/**
* <p>
* 模拟JDK动态生成的代理类
* </p>
* @author linanmiao
* @date 2017年4月6日
* @versio 1.0
*/
public class DSubject$Proxy1 extends java.lang.reflect.Proxy implements DSubject{

	private static final long serialVersionUID = 6620396656353468896L;
	
	private static Method equalsMethod;
	private static Method hashCodeMethod;
	private static Method toStringMethod;
	private static Method invokeMethod;
	
	static{
		try {
			equalsMethod = Class.forName("java.lang.Object").getMethod("equals", new Class<?>[]{Class.forName("java.lang.Object")});
			hashCodeMethod = Class.forName("java.lang.Object").getMethod("hashCode", new Class<?>[0]);
			toStringMethod = Class.forName("java.lang.Object").getMethod("toString", new Class<?>[0]);
			invokeMethod = Class.forName("lam.design.pattern.proxy.dynamic.DSubject")
						.getMethod("invokeMethod", new Class<?>[]{Class.forName("java.lang.String")});
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public DSubject$Proxy1(InvocationHandler h) {
		super(h);
	}

	@Override
	public String invokeMethod(String argument) {
		try {
			return (String)h.invoke(this, invokeMethod, new Object[]{argument});
		} catch (Throwable e) {
			throw new UndeclaredThrowableException(e);
		}
	}
	
	@Override
	public final boolean equals(Object obj) {
		try {
			return ((Boolean)h.invoke(this, equalsMethod, new Object[]{obj})).booleanValue();
		} catch (Throwable e) {
			throw new UndeclaredThrowableException(e);
		}
	}
	
	@Override
	public final int hashCode() {
		try {
			return ((Integer)h.invoke(this, hashCodeMethod, null)).intValue();
		} catch (Throwable e) {
			throw new UndeclaredThrowableException(e);
		}
	}
	
	@Override
	public final String toString() {
		try {
			return (String)h.invoke(this, toStringMethod, null);
		} catch (Throwable e) {
			throw new UndeclaredThrowableException(e);
		}
	}

}
