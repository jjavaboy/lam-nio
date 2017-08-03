package org.lam.mq;

import java.net.URI;
import java.net.URISyntaxException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public class App {
    public static void main( String[] args ) throws JMSException, URISyntaxException{
    	//sendQueue();
    	//receiveQueue();
    	sendTopic();
    	//receiveTopic();
    }
    
    private static void sendQueue() throws JMSException, URISyntaxException{
    	final ConnectionFactory factory = new ActiveMQConnectionFactory(new URI("tcp://192.168.204.79:61616"));
    	final Connection conn = factory.createConnection();
    	final Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
    	final Destination dest = session.createQueue("FirstQueue");
    	final MessageProducer producer = session.createProducer(dest);
    	producer.setDeliveryMode(DeliveryMode.PERSISTENT);
    	final Message message = session.createTextMessage("Message of FirstQueue.");
    	producer.send(message);
    	conn.close();
    }
    
    private static void sendTopic() throws URISyntaxException, JMSException{
    	ConnectionFactory factory = new ActiveMQConnectionFactory(new URI("tcp://192.168.204.79:61616"));
    	Connection conn = factory.createConnection();
    	Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
    	Destination desc = session.createTopic("FirstTopic");
    	MessageProducer producer = session.createProducer(desc);
    	producer.setDeliveryMode(DeliveryMode.PERSISTENT);
    	Message message = session.createTextMessage("Mesage of FirstTopic");
    	producer.send(message);
    	conn.close();
    }
}
