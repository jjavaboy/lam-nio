import lam.design.pattern.proxy.SubjectProxyFactory;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年4月5日
* @version 1.0
*/
public class ProxySubjectTest {
	
	public static void main(String[] args){
		SubjectProxyFactory.getInstance().newProxyInstance().doWork("I'm argument, lalala.");
	}

}
