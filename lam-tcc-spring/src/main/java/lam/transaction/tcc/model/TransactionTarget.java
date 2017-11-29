package lam.transaction.tcc.model;

import java.io.Serializable;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年11月29日
* @version 1.0
*/
public class TransactionTarget implements Serializable{
	
	private static final long serialVersionUID = -803635358036980716L;

	private final Object target;
	
	//private final Method method;
	private String method;
	
	private Object[] args;
	
	public TransactionTarget(Object target) {
		this.target = target;
	}

	public Object getTarget() {
		return target;
	}

	public TransactionTarget setMethod(String method) {
		this.method = method;
		return this;
	}
	
	public String getMethod() {
		return method;
	}
	
	public TransactionTarget setArgs(Object[] args) {
		this.args = args;
		return this;
	}
	
	public Object[] getArgs() {
		return args;
	}
}
