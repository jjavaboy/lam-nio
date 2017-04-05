package lam.design.pattern.proxy;
/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年4月5日
* @version 1.0
*/
public class RealSubjectFactory implements SubjectFactory{

	@Override
	public Subject createSubject() {
		return new ProxySubject(new RealSubject());
	}

}
