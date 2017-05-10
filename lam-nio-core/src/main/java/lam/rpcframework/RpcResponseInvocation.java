package lam.rpcframework;

import lam.rpcframework.support.Invocation;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年5月10日
* @version 1.0
*/
public class RpcResponseInvocation implements Invocation{

	private static final long serialVersionUID = -4796796614283841149L;
	
	Object result;
	
	public RpcResponseInvocation(Object result){
		this.result = result;
	}

	@Override
	public String getInterfac() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Class<?> getReturnType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getMethodName() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Class<?>[] getParameterTypes() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object[] getParameters() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Class<?>[] getExceptionTypes() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getResult() {
		return this.result;
	}
}
