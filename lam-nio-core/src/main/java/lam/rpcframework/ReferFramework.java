package lam.rpcframework;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Arrays;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
				send(socket, method, args);
				Object result = receive(socket);
				
				
				return result;
			} finally {
				FinalizeUtils.closeQuietly(socket);
			}
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
