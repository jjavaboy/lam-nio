package lam.transaction.tcc.exception;
/**
* <p>
* throw cancel exception when tcc execute cancel method.
* </p>
* @author linanmiao
* @date 2017年11月29日
* @version 1.0
*/
public class CanceledException extends RuntimeException{

	private static final long serialVersionUID = 1961360649350463100L;
	
	public CanceledException() {
		
	}
	
	public CanceledException(String message) {
		super(message);
	}
	
	public CanceledException(Throwable t) {
		super(t);
	}
	
	public CanceledException(String message, Throwable t) {
		super(message, t);
	}

}
