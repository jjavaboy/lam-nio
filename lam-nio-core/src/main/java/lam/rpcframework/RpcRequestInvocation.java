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
public class RpcRequestInvocation implements Invocation{

	private static final long serialVersionUID = -4796796614283841149L;
	
	String interfac; 
	Class<?> returnType; 
	String methodName; 
	Class<?>[] parameterTypes; 
	Object[] parameters; 
	Class<?>[] exceptionTypes;
	
	public RpcRequestInvocation(String interfac, Class<?> returnType, String methodName, 
			Class<?>[] parameterTypes, Object[] parameters, Class<?>[] exceptionTypes){
		this.interfac = interfac;
		this.returnType = returnType;
		this.methodName = methodName;
		this.parameterTypes = parameterTypes;
		this.parameters = parameters;
		this.exceptionTypes = exceptionTypes;
	}

	@Override
	public String getInterfac() {
		return this.interfac;
	}

	@Override
	public Class<?> getReturnType() {
		return this.returnType;
	}

	@Override
	public String getMethodName() {
		return this.methodName;
	}

	@Override
	public Class<?>[] getParameterTypes() {
		return this.parameterTypes;
	}

	@Override
	public Object[] getParameters() {
		return this.parameters;
	}

	@Override
	public Class<?>[] getExceptionTypes() {
		return this.exceptionTypes;
	}

	@Override
	public Object getResult() {
		throw new UnsupportedOperationException();
	}
}
