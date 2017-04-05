import lam.design.pattern.proxy.dynamic.DRealSubject;
import lam.design.pattern.proxy.dynamic.DSubject;
import lam.design.pattern.proxy.dynamic.DSubjectProxyFactory;

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
		DSubject ds = new DRealSubject();
		Class<?>[] classes = ds.getClass().getInterfaces();
		for(Class<?> clazz : classes){
			System.out.println(clazz.getName());
		}
		sun.misc.Launcher l = null;
		System.out.println(ds.getClass().getClassLoader());
	}

}
