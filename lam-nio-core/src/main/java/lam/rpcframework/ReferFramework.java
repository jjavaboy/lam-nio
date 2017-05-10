package lam.rpcframework;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Arrays;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

import lam.rpcframework.serialize.RpcHessian2Input;
import lam.rpcframework.serialize.RpcHessian2Output;
import lam.rpcframework.support.Invocation;
import lam.rpcframework.support.Referable;
import lam.util.FinalizeUtils;

/**
* <p>
* refer framework
* </p>
* @author linanmiao
* @date 2017年4月11日
* @version 1.0
*/
public class ReferFramework implements Referable{
	
	private static Logger logger = LoggerFactory.getLogger(ReferFramework.class);

	@Override
	public <T> T refer(final Class<T> interfaceClass, final String host, final int port) throws Exception {
		Objects.requireNonNull(interfaceClass, "interfaceClass must not be null");
		Objects.requireNonNull(host, "host must not be null");
		if(port <= 0 || port > 65535){
			throw new IllegalArgumentException("port:" + port + " is out of range[1 - 65535]");
		}
		
		return (T) java.lang.reflect.Proxy.newProxyInstance(
					interfaceClass.getClassLoader(), 
					new Class<?>[]{interfaceClass}, 
					new ClientInvocationHandler(host, port)
					);
	}
	
	private class ClientInvocationHandler implements InvocationHandler{
		
		private final String host;
		private final int port;
		
		ClientInvocationHandler(final String host, final int port){
			this.host = host;
			this.port = port;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			Socket socket = null;
			try{
				socket = new Socket(host, port);
				//send(socket, method, args);
				send2Hessian(socket, proxy, method, args);
				//Object result = receive(socket);
				Object result = receive2Hessian(socket);
				return result;
			} finally {
				FinalizeUtils.closeQuietly(socket);
			}
		}
		
		private void send2Hessian(final Socket socket, Object proxy, final Method method, final Object[] parameters){
			Class<?> interfa = null;
			Class<?>[] interfaces = proxy.getClass().getInterfaces();
			for(Class<?> interfac : interfaces){
				if(contains(interfac.getMethods(), method)){
					interfa = interfac;
					break ;
				}
			}
			Objects.requireNonNull(interfa, "cann't find the interface with method " + method);
			try {
				Invocation invocation = new RpcRequestInvocation(
						interfa.getName(), method.getReturnType(), method.getName(),
						method.getParameterTypes(), parameters, method.getExceptionTypes());
				Hessian2Output output = new RpcHessian2Output(socket.getOutputStream());
				output.writeObject(invocation);
				output.flush();
				output.close();
			} catch (IOException e) {
				logger.error("error", e);
			}
		}
		
		private boolean contains(Method[] methods, final Method method){
			for(Method m : methods){
				if(m.equals(method)){
					return true;
				}
			}
			return false;
		}
		
		private Object receive2Hessian(final Socket socket){
			Object result = null;
			ObjectInputStream objectInputStream = null;
			try {
				Hessian2Input input = new RpcHessian2Input(socket.getInputStream());
				Invocation invocation = (Invocation) input.readObject();
				
				result = invocation != null ? invocation.getResult() : null;
				
				logger.info("receive==>>" + result);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				//FinalizeUtils.closeQuietly(objectInputStream);
			}
			return result;
		}
		
		private void send(final Socket socket, final Method method, final Object[] parameters){
			ObjectOutputStream objectOutputStream = null;
			try {
				objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
				objectOutputStream.writeUTF(method.getName());              //methodName
				objectOutputStream.writeObject(method.getParameterTypes()); //parameterTypes
				objectOutputStream.writeObject(parameters);                 //parameters
				objectOutputStream.flush();
				
				logger.info("send==>>method:" + method.getName() 
					+ ", parameterTypes:" + Arrays.toString(method.getParameterTypes())
					+ ", parameters:" + Arrays.toString(parameters));
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				//FinalizeUtils.closeQuietly(objectOutputStream);
			}
		}
		
		private Object receive(final Socket socket){
			Object result = null;
			ObjectInputStream objectInputStream = null;
			try {
				objectInputStream = new ObjectInputStream(socket.getInputStream());
				result = objectInputStream.readObject();
				
				logger.info("receive==>>" + result);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				//FinalizeUtils.closeQuietly(objectInputStream);
			}
			return result;
		}
	}
	

}
