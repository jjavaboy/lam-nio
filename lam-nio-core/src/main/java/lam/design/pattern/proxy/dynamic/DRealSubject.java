package lam.design.pattern.proxy.dynamic;
/**
* <p>
* dynamic
* </p>
* @author linanmiao
* @date 2017年4月5日
* @version 1.0
*/
public class DRealSubject implements DSubject{

	@Override
	public String invokeMethod(String argument) {
		int i = 0;
		for(int j = 0; j < 100000000; j++){
			i++;
		}
		return "success";
	}

}
