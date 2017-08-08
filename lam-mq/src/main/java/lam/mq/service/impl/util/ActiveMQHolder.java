package lam.mq.service.impl.util;

import java.net.URI;
import java.net.URISyntaxException;

import javax.jms.Connection;
import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年8月3日
* @version 1.0
*/
public class ActiveMQHolder {
	
	//private javax.jms.ConnectionFactory factory;
	private ActiveMQConnectionFactory factory;
	
	private ActiveMQHolder(){
		init();
	}
	
	private void init(){
		try {
			factory = new ActiveMQConnectionFactory(new URI("tcp://192.168.204.79:61616"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() throws JMSException{
		return factory.createConnection();
	}
	
	public ActiveMQConnection createConnection() throws JMSException{
		return (ActiveMQConnection) factory.createConnection();
	}
	
	public static ActiveMQHolder getInstance(){
		return InstanceHolder.INSTANCE;
	}
	
	private static class InstanceHolder{
		private static ActiveMQHolder INSTANCE = new ActiveMQHolder(); 
	}

}
