package lam.design.pattern.chainofresponsibility.dubbo;

import java.util.List;

import lam.util.Strings;

/**
* <p>
* invoker builder
* </p>
* @author linanmiao
* @date 2017年7月6日
* @version 1.0
*/
public class InvokerBuilder {
	
	public static Invoker buildInvokerChain(Invoker invoker, List<Filter> filters){
		Invoker last = invoker;
		if(Strings.isNullOrEmpty(filters))
			return last;
		for(int i = filters.size() - 1; i >= 0; i--){
			final Filter filter = filters.get(i);
			final Invoker next = last;
			last = new Invoker(){
				@Override
				public Object invoke(Object param) throws Exception {
					return filter.invoke(next, param);
				}
			};
		}
		return last;
	}

}
