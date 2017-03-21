package lam.pool;

import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
* <p>
* socket factory
* </p>
* @author linanmiao
* @date 2017年3月21日
* @versio 1.0
*/
public class SSocketFactory implements PooledObjectFactory<SSocket>{
	
	private String host;
	private int port;
	
	public SSocketFactory(String host, int port){
		this.host = host;
		this.port = port;
	}

	@Override
	public PooledObject<SSocket> makeObject() throws Exception {
		/*SSocket ssocket = new SSocket.Builder()
				.setHost(this.host)
				.setPort(this.port)
				.buid();*/
		SSocket ssocket;
		try{
			ssocket = new SSocket(this.host, this.port);
		}catch(UnknownHostException unk){
			throw new SSocketException("unknow host:" + this.port, unk);
		}catch(IOException ioe){
			throw new SSocketException("io exception", ioe);
		}
		
		return new DefaultPooledObject<SSocket>(ssocket);
	}

	@Override
	public void destroyObject(PooledObject<SSocket> p) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean validateObject(PooledObject<SSocket> p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void activateObject(PooledObject<SSocket> p) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void passivateObject(PooledObject<SSocket> p) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
