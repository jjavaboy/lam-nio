package lam.pool;

import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import lam.log.Console;
import lam.pool.support.SDefaultPooledObject;
import lam.pool.support.SPooledObject;
import lam.pool.support.SPooledObjectFactory;

/**
* <p>
* socket factory
* </p>
* @author linanmiao
* @date 2017年3月21日
* @versio 1.0
*/
public class SSocketFactory extends  SPooledObjectFactory<SSocket> /*implements PooledObjectFactory<SSocket>*/{
	
	private String host;
	private int port;
	
	public SSocketFactory(String host, int port){
		this.host = host;
		this.port = port;
	}
	
	//================implements SPooledObjectFactory===================================

	@Override
	public SPooledObject<SSocket> makeSObject() throws Exception {
		SSocket ssocket;
		try{
			ssocket = new SSocket(this.host, this.port);
		}catch(UnknownHostException unk){
			throw new SSocketException("unknow host:" + this.port, unk);
		}catch(IOException ioe){
			throw new SSocketException("io exception", ioe);
		}
		return new SDefaultPooledObject<SSocket>(ssocket);
	}

	@Override
	public void destroySObject(SPooledObject<SSocket> p) throws Exception {
		SSocket s = p.getSObject();
		if(s != null){
			s.disConnect();
		}
	}

	@Override
	public boolean validateSObject(SPooledObject<SSocket> p) {
		SSocket s = p.getSObject();
		return s != null && s.isConnected();
	}

	@Override
	public void activateSObject(SPooledObject<SSocket> p) throws Exception {
		SSocket ssocket = p.getSObject();
		//To do what?
		//Console.println("activateSObject:SSocket:" + ssocket);
	}

	@Override
	public void passivateSObject(SPooledObject<SSocket> p) throws Exception {
		//passivate object
		SSocket ssocket = p.getSObject();
		//Console.println("passivateSObject:SSocket:" + ssocket);
	}
	
	//================implements PooledObjectFactory====================================

	/*@Override
	public PooledObject<SSocket> makeObject() throws Exception {
		SSocket ssocket = new SSocket.Builder()
				.setHost(this.host)
				.setPort(this.port)
				.build();
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
		SSocket ssocket = p.getObject();
		if(ssocket != null){
			ssocket.disConnect();
		}
	}

	@Override
	public boolean validateObject(PooledObject<SSocket> p) {
		SSocket ssocket = p.getObject();
		return ssocket != null && ssocket.isConnected();
	}

	@Override
	public void activateObject(PooledObject<SSocket> p) throws Exception {
		SSocket ssocket = p.getObject();
		//To do what?
		Console.println("activateObject:SSocket:" + ssocket);
	}

	@Override
	public void passivateObject(PooledObject<SSocket> p) throws Exception {
		//passivate object
		SSocket ssocket = p.getObject();
		Console.println("passivateObject:SSocket:" + ssocket);
	}*/


}
