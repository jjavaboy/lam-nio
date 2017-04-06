import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;

import lam.design.pattern.proxy.dynamic.DRealSubject;
import lam.design.pattern.proxy.dynamic.DSubject;
import lam.design.pattern.proxy.dynamic.DSubjectInvocationHandler;
import lam.design.pattern.proxy.dynamic.DSubjectProxyFactory;
import lam.util.FinalizeUtils;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年4月5日
* @version 1.0
*/
public class DProxySubjectTest {
	
	public static void main(String[] args){
		DSubjectProxyFactory.getInstance().newProxyInstance().invokeMethod("gogo");
		
		//===================================
		DSubject dSubject = new DRealSubject();
		InvocationHandler h = new DSubjectInvocationHandler(dSubject);
		
		String proxyName = DSubject.class.getName() + "$Proxy" + "1";
		Class<?>[] interfaces = dSubject.getClass().getInterfaces();
		byte[] classBytes = sun.misc.ProxyGenerator.generateProxyClass(proxyName, interfaces);
		Class<?> clazz = new ProxyClassLoader().defineClass0(null, classBytes, 0, classBytes.length);
		
		java.lang.reflect.Constructor<?> c = getConstructor(clazz);
		DSubject ds = (DSubject) newInstance(c, h);
		ds.invokeMethod("gogo");
		
		writeByteToDisk(classBytes);
	}
	
	private static void writeByteToDisk(byte[] classBytes) {
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream("D:\\opt\\lam.design.pattern.proxy.dynamic.DSubject$Proxy1.class");
			outputStream.write(classBytes, 0, classBytes.length);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			FinalizeUtils.closeQuietly(outputStream);
		}
		
	}

	private static java.lang.reflect.Constructor<?> getConstructor(Class<?> clazz){
		Class<?>[] parameterTypes = new Class<?>[]{InvocationHandler.class};
		try {
			return clazz.getConstructor(parameterTypes);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static Object newInstance(Constructor<?> c, InvocationHandler h){
		try {
			return c.newInstance(h);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static class ProxyClassLoader extends ClassLoader{
		public Class<?> defineClass0(String name, byte[] b, int off, int len){
			return super.defineClass(name, b, off, len);
		}
	}

}
