package lam.dubbo.loadbalance.support;
/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年5月3日
* @version 1.0
*/
public class RpcException extends RuntimeException{

	private static final long serialVersionUID = -3240306760586885086L;

	public RpcException(){
		//super();
		//call super() by default
	}
	
	public RpcException(String msg){
		super(msg);
	}
	
	public RpcException(Throwable t){
		super(t);
	}
	
	public RpcException(String msg, Throwable t){
		super(msg, t);
	}
	
	public boolean isBusinessException(){
		return true;
	}
	
}
