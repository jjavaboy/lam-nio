package lam.design.pattern.proxy;
/**
* <p>
* real subject
* </p>
* @author linanmiao
* @date 2017年4月5日
* @version 1.0
*/
public class RealSubject implements Subject{

	@Override
	public boolean doWork(String argument) {
		for(int i = 0; i < 5000000; i++){
		}
		return true;
	}

}
