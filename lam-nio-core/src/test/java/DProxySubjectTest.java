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
	}

}
