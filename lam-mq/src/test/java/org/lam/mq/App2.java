package org.lam.mq;

import java.net.URI;
import java.net.URISyntaxException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

public class App2 {
    public static void main( String[] args ) throws JMSException, URISyntaxException{
    	receiveTopic();
    }
    
    private static void receiveQueue() throws JMSException, URISyntaxException{
    	ConnectionFactory factory = new ActiveMQConnectionFactory(new URI("tcp://192.168.204.79:61616"));
    	Connection conn = factory.createConnection();
    	Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
    	Destination dest = session.createQueue("FirstQueue");
    	MessageConsumer consumer = session.createConsumer(dest);
    	conn.start();
    	Message message = consumer.receive();
    	System.out.println(message.toString());
    	conn.close();
    }
    
    private static void receiveTopic() throws URISyntaxException, JMSException{
    	ConnectionFactory factory = new ActiveMQConnectionFactory(new URI("tcp://192.168.204.79:61616"));
    	Connection conn = factory.createConnection();
    	Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
    	Destination desc = session.createTopic("FirstTopic");
    	MessageConsumer consumer = session.createConsumer(desc);
    	conn.start();
    	while(true){
    	Message message = consumer.receive();
    	System.out.println(message.toString());
    	}
    	//conn.close();
    }
    
    /*private static void receiveTopicSubscriber() throws URISyntaxException, JMSException{
    	ConnectionFactory factory = new ActiveMQConnectionFactory(new URI("tcp://192.168.204.79:61616"));
    	Connection conn = factory.createConnection();
    	conn.setClientID("FirstTopicClient");
    	Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
    	Topic topic = session.createTopic("FirstTopic");
    	MessageConsumer consumer = session.createDurableSubscriber(topic, "FirstSubscriber");
    	conn.start();
    	Message message = consumer.receive();
    	System.out.println(message.toString());
    	conn.close();
    }*/
}
