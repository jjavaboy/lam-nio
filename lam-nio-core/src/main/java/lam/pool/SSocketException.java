package lam.pool;
/**
* <p>
* defined an exception class
* </p>
* @author linanmiao
* @date 2017年3月21日
* @versio 1.0
*/
public class SSocketException extends RuntimeException{

	private static final long serialVersionUID = -5501924764692184948L;
	
	public SSocketException(){
		super();//auto call super()
	}
	
	public SSocketException(String message){
		super(message);
	}
	
	public SSocketException(String message, Throwable cause){
		super(message, cause);
	}
	
	public SSocketException(Throwable cause){
		super(cause);
	}

}
