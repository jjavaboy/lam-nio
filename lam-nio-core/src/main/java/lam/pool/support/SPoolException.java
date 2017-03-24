package lam.pool.support;
/**
* <p>
* </p>
* @author linanmiao
* @date 2017年3月24日
* @version 1.0
*/
public class SPoolException extends Exception{

	private static final long serialVersionUID = 7778836217083973014L;

	public SPoolException(){
		super();
	}
	
	public SPoolException(String msg){
		super(msg);
	}
	
	public SPoolException(Throwable t){
		super(t);
	}
	
	public SPoolException(String msg, Throwable t){
		super(msg, t);
	}
	
}
