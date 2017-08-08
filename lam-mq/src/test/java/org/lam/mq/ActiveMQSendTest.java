package org.lam.mq;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.broker.region.policy.RedeliveryPolicyMap;
import org.apache.activemq.command.ActiveMQQueue;

public class ActiveMQSendTest {
    public static void main( String[] args ) throws JMSException, URISyntaxException{
    	sendQueue();
    	//receiveQueue();
    	//sendTopic();
    	//receiveTopic();
    }
    
    private static void sendQueue() throws JMSException, URISyntaxException{
    	final ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(new URI("tcp://192.168.204.79:61616"));
    	final ActiveMQConnection conn = (ActiveMQConnection) factory.createConnection();
    	
    	//http://activemq.apache.org/redelivery-policy.html
    	RedeliveryPolicy queuePolicy = new RedeliveryPolicy();
    	queuePolicy.setInitialRedeliveryDelay(0);
    	queuePolicy.setRedeliveryDelay(5000L);
    	queuePolicy.setUseExponentialBackOff(Boolean.FALSE.booleanValue());
    	//setMaximumRedeliveries，指第一次客户端消费消息失败后，broker会重试6次，推送消息给客户端
    	//重试6次后还是失败，消息会进入ActiveMQ的"Dead Letter Queue"，队列名默认叫"ActiveMQ.DLQ"，
    	queuePolicy.setMaximumRedeliveries(6);
    	
    	RedeliveryPolicyMap redeliveryPolicyMap = new RedeliveryPolicyMap();
    	redeliveryPolicyMap.put(new ActiveMQQueue("FirstQueue"), queuePolicy);
    	
    	//设置消息消费失败后的重试策略
    	conn.setRedeliveryPolicyMap(redeliveryPolicyMap);
    	
    	long timeSecond = System.currentTimeMillis() / 1000;
    	
    	final Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
    	final Destination dest = session.createQueue("FirstQueue");
    	final MessageProducer producer = session.createProducer(dest);
    	producer.setDeliveryMode(DeliveryMode.PERSISTENT);
    	final TextMessage message = session.createTextMessage("Message of FirstQueue in " + timeSecond);
    	message.setJMSMessageID(String.format("%s-%d", "FirstQueue", timeSecond));
    	message.setJMSRedelivered(Boolean.TRUE.booleanValue());
    	
    	producer.send(message);
    	
    	Destination destination = session.createQueue("DLQ:TEST");
    	MessageProducer dlqProducer = session.createProducer(destination);
    	TextMessage textMessage = session.createTextMessage("TEST"  + System.currentTimeMillis());
    	textMessage.setJMSRedelivered(Boolean.TRUE.booleanValue());
    	dlqProducer.send(textMessage);
    	
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
