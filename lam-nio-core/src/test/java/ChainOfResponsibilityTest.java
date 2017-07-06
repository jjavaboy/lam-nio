import java.util.ArrayList;
import java.util.List;

import lam.design.pattern.chainofresponsibility.dubbo.Filter;
import lam.design.pattern.chainofresponsibility.dubbo.Invoker;
import lam.design.pattern.chainofresponsibility.dubbo.InvokerBuilder;
import lam.design.pattern.chainofresponsibility.rh.AbstractHandler;
import lam.design.pattern.chainofresponsibility.rh.AbstractRequest;
import lam.design.pattern.chainofresponsibility.rh.HandlerL01;
import lam.design.pattern.chainofresponsibility.rh.HandlerL02;
import lam.design.pattern.chainofresponsibility.rh.HandlerL03;
import lam.design.pattern.chainofresponsibility.rh.RequestL01;
import lam.design.pattern.chainofresponsibility.rh.RequestL02;
import lam.design.pattern.chainofresponsibility.rh.RequestL03;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年4月27日
* @versio 1.0
*/
public class ChainOfResponsibilityTest {
	
	public static void main(String[] args){
		List<Filter> filters = new ArrayList<Filter>();
		for(int i = 0; i < 5; i++){
			final int finalI = i;
			filters.add(new Filter(){
				private int i = finalI;
				@Override
				public Object invoke(Invoker invoker, Object object) throws Exception {
					System.out.println(getClass().getName() + ".invoke i:" + this.i);
					return invoker.invoke(object);
				}});
		}
		Invoker invoker = InvokerBuilder.buildInvokerChain(new DetectorInvoker(), filters);
		filters.clear();
		for(int i = 6; i < 11; i++){
			final int finalI = i;
			filters.add(new Filter(){
				private int i = finalI;
				@Override
				public Object invoke(Invoker invoker, Object object) throws Exception {
					Object r = invoker.invoke(object);
					System.out.println(getClass().getName() + ".invoke i:" + this.i);
					return r;
				}});
		}
		invoker = InvokerBuilder.buildInvokerChain(invoker, filters);
		try {
			System.out.println(invoker.invoke("parameter"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static class DetectorInvoker implements Invoker{

		@Override
		public Object invoke(Object param) throws Exception {
			System.out.println(getClass().getName() + ".invoke service代理类 执行了");
			return "result of " + param;
		}
		
	}

}
