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
		AbstractHandler handlerL01 = new HandlerL01();
		AbstractHandler handlerL02 = new HandlerL02();
		AbstractHandler handlerL03 = new HandlerL03();
		
		handlerL01.setNext(handlerL02);
		handlerL02.setNext(handlerL03);
		
		AbstractRequest requestL01 = new RequestL01();
		AbstractRequest requestL02 = new RequestL02();
		AbstractRequest requestL03 = new RequestL03();
		
		handlerL01.run(requestL01);
		handlerL01.run(requestL02);
		handlerL01.run(requestL03);
	}

}
