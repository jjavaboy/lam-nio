package lam.rpcframework;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

import lam.rpcframework.serialize.RpcHessian2Input;
import lam.rpcframework.serialize.RpcHessian2Output;
import lam.rpcframework.support.Exportable;
import lam.rpcframework.support.Invocation;
import lam.util.FinalizeUtils;

/**
* <p>
* export framework
* </p>
* @author linanmiao
* @date 2017年4月11日
* @version 1.0
*/
public class ExportFramework implements Exportable{
	
	private static Logger logger = LoggerFactory.getLogger(ExportFramework.class);

	@Override
	public void export(final Object object, final int port) throws Exception {
		Objects.requireNonNull(object, "argument object must not be null.");
		if(port <= 0 || port > 65535){
			throw new IllegalArgumentException("port:" + port + " is out of range[1 - 65535]");
		}
		new Thread(new ServerThread(object, port)).start();
	}
	
	public class ServerThread implements Runnable{
		
		final Object object;
		
		final ServerSocket serverSocket;
		
		ServerThread(final Object object, final int port){
			this.object = object;
			try {
				serverSocket = new ServerSocket(port);
				logger.info("Export service " + object.getClass().getName() + " on port " + port);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void run() {
			while(true){
				Socket socket = null;
				try {
					//accept:It will be blocking util an socket client come.
					socket = serverSocket.accept();
					//Object result = receive(socket);
					Object result = receive2Hessian(socket);
					//send(socket, result);
					send2Hessian(socket, result);
				} catch (Exception e) {
					logger.error("error", e);
				} finally {
					FinalizeUtils.closeQuietly(socket);
				}
			}
		}
		
		private Object receive(final Socket socket){
			Object result = "Error";
			ObjectInputStream objectInputStream = null;
			try {
				objectInputStream = new ObjectInputStream(socket.getInputStream());
				String     methodName     = objectInputStream.readUTF();
				Class<?>[] parameterTypes = (Class<?>[]) objectInputStream.readObject();
				Object[]   parameters     = (Object[]) objectInputStream.readObject();
				
				try {
					Method method = object.getClass().getMethod(methodName, parameterTypes);
					result = method.invoke(object, parameters);
				} catch (NoSuchMethodException e) {
					result = "No such method:" + methodName;
				} catch (SecurityException e) {
					result = e.getMessage();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					result = "Argument is illegal";
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				//FinalizeUtils.closeQuietly(objectInputStream);
			}
			return result;
		}
		
		private Object receive2Hessian(final Socket socket){
			Object result = "Error";
			try {
				Hessian2Input input = new RpcHessian2Input(socket.getInputStream());
				Invocation invocation = (Invocation) input.readObject();
				
				Throwable t = null;
				try {
					Method method = object.getClass().getMethod(invocation.getMethodName(), invocation.getParameterTypes());
					result = method.invoke(object, invocation.getParameters());
				} catch (NoSuchMethodException e) {
					t = e;
					result = "No such method:" + invocation.getMethodName();
				} catch (SecurityException e) {
					t = e;
					result = e.getMessage();
				} catch (IllegalAccessException e) {
					t = e;
				} catch (IllegalArgumentException e) {
					t = e;
					result = "Argument is illegal";
				} catch (InvocationTargetException e) {
					t = e;
				}
				if(t != null){
					logger.error("invoke method:" + invocation.getMethodName() + " error.", t);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				//FinalizeUtils.closeQuietly(objectInputStream);
			}
			return result;
		}
		
		private void send2Hessian(final Socket socket, Object result){
			try {
				Invocation invocation = new RpcResponseInvocation(result);
				Hessian2Output output = new RpcHessian2Output(socket.getOutputStream());
				output.writeObject(invocation);
				output.flush();
				output.close();
			} catch (IOException e) {
				logger.error("error", e);
			}
		}
	
		private void send(final Socket socket, Object result){
			ObjectOutputStream objectOutputStream = null;
			try {
				objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
				objectOutputStream.writeObject(result);
				objectOutputStream.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				//FinalizeUtils.closeQuietly(objectOutputStream);
			}
		}
	}

}
