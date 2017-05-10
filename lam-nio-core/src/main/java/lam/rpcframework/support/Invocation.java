package lam.rpcframework.support;

import java.io.Serializable;

/**
* <p>
* invocation of method
* </p>
* @author linanmiao
* @date 2017年5月10日
* @version 1.0
*/
public interface Invocation extends Serializable{
	
	public String getInterfac();
	
	public Class<?> getReturnType();
	
	public String getMethodName();
	
	public Class<?>[] getParameterTypes();
	
	public Object[] getParameters();
	
	public Class<?>[] getExceptionTypes();
	
	public Object getResult();

}
