package lam.pool;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年3月21日
* @versio 1.0
*/
public class SSocket extends Socket{
	
	protected boolean broken;
	
	public SSocket(String host, int port) throws UnknownHostException, IOException{
		super(host, port);
	}
	
	public SSocket(InetSocketAddress inetSocketAddress) throws UnknownHostException, IOException{
		this(inetSocketAddress.getHostName(), inetSocketAddress.getPort());
	}
	
	public SSocket(Builder builder) throws UnknownHostException, IOException{
		this(builder.host, builder.port);
		setTcpNoDelay(builder.tcpNoDelay);
		setSoTimeout(builder.soTimeout);
		setKeepAlive(builder.keepAlive);
		setOOBInline(builder.oobInline);
		setReceiveBufferSize(builder.receiveBufferSize);
		setReuseAddress(builder.reuseAddress);
		setSendBufferSize(builder.sendBufferSize);
	}
	
	public void disConnect(){
		if(isConnected()){
			try {
				OutputStream outputStream = super.getOutputStream();
				if(outputStream != null){
					outputStream.flush();
				}
				//super.close();
			} catch (IOException e) {
				broken = true;
				e.printStackTrace();
			} finally {
				try{
					super.close();
				}catch(Exception e){
				}
			}
		}
	}
	
	public boolean isConnected(){
		return super.isBound() && !super.isClosed() && super.isConnected() && !super.isInputShutdown() && !super.isOutputShutdown();
	}
	
	public static class Builder{
		private String host;
		private int port;
		private boolean tcpNoDelay;
		private int soTimeout;
		private boolean keepAlive;
		private boolean oobInline;
		private int receiveBufferSize;
		private boolean reuseAddress;
		private int sendBufferSize;
		
		public Builder setHost(String host) {
			this.host = host;
			return this;
		}
		
		public Builder setPort(int port) {
			this.port = port;
			return this;
		}
		
		public Builder setTcpNoDelay(boolean tcpNoDelay) {
			this.tcpNoDelay = tcpNoDelay;
			return this;
		}
		
		public Builder setSoTimeout(int soTimeout) {
			this.soTimeout = soTimeout;
			return this;
		}
		
		public Builder setKeepAlive(boolean keepAlive) {
			this.keepAlive = keepAlive;
			return this;
		}
		
		public Builder setOobInline(boolean oobInline) {
			this.oobInline = oobInline;
			return this;
		}
		
		public Builder setReceiveBufferSize(int receiveBufferSize) {
			this.receiveBufferSize = receiveBufferSize;
			return this;
		}
		
		public Builder setReuseAddress(boolean reuseAddress) {
			this.reuseAddress = reuseAddress;
			return this;
		}
		
		public Builder setSendBufferSize(int sendBufferSize) {
			this.sendBufferSize = sendBufferSize;
			return this;
		}
		
		public SSocket build() throws UnknownHostException, IOException{
			return new SSocket(this);
		}
	}
	
}
