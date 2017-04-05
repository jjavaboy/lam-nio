import lam.design.pattern.proxy.RealSubjectFactory;
import lam.design.pattern.proxy.Subject;
import lam.design.pattern.proxy.SubjectFactory;

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
		SubjectFactory factory = new RealSubjectFactory();
		Subject subject = factory.createSubject();
		subject.doWork("I'm argument, lalala.");
	}

}
