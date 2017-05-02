package lam.dubbo.loadbalance.support;

import java.util.List;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年5月2日
* @version 1.0
*/
public interface LoadBalance {
	
	public Invoker doSelect(List<Invoker> invokers);

}
