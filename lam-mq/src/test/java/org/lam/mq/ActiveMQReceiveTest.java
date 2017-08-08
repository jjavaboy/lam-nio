package org.lam.mq;

import java.net.URI;
import java.net.URISyntaxException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

public class ActiveMQReceiveTest {
	
	private static int time = 0;
	
    public static void main( String[] args ) throws JMSException, URISyntaxException{
    	receiveQueue();
    	//receiveTopic();
    }
    
    private static void receiveQueue() throws JMSException, URISyntaxException{
    	ConnectionFactory factory = new ActiveMQConnectionFactory(new URI("tcp://192.168.204.79:61616"));
    	Connection conn = factory.createConnection();
    	Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
    	
    	conn.start();
    	Destination dest = session.createQueue("FirstQueue");
    	MessageConsumer consumer = session.createConsumer(dest);
    	/*
    	Message message = consumer.receive();
    	System.out.println(message.toString());
    	*/
    	consumer.setMessageListener(new MessageListener(){
			@Override
			public void onMessage(Message message) {
				System.out.println("time:" + ++time + "," + message.toString());
				//抛出异常，则表示消费"FirstQueue"消息失败，如果消息设置了重试策略，稍后会重试推送过来客户端。
				throw new RuntimeException("error, time:" + time + "," + message.toString());
			}});
    	
    	Destination dlqTest = session.createQueue("DLQ:TEST");
    	MessageConsumer dlqTestConsumer = session.createConsumer(dlqTest);
    	dlqTestConsumer.setMessageListener(new MessageListener(){
			@Override
			public void onMessage(Message message) {
				//抛出异常，则表示消费"DLQ:TEST"消息失败，如果消息设置了重试策略，稍后会重试推送过来客户端。
				throw new RuntimeException("DLQ:TEST, error, message:" + message.toString());
			}});
    	
    	//可以在ActiveMQ的配置文件配置DLQ:前缀的QUEUE才会进入"DLQ"队列。//http://activemq.apache.org/message-redelivery-and-dlq-handling.html
    	/**
    	<broker>
		  <destinationPolicy>
		    <policyMap>
		      <policyEntries>
		        <!-- Set the following policy on all queues using the '>' wildcard -->
		        <policyEntry queue=">">
		          <deadLetterStrategy>
		            <!--
		              Use the prefix 'DLQ.' for the destination name, and make
		              the DLQ a queue rather than a topic
		            -->
		            <individualDeadLetterStrategy queuePrefix="DLQ." useQueueForQueueMessages="true"/>
		          </deadLetterStrategy>
		        </policyEntry>
		      </policyEntries>
		    </policyMap>
		  </destinationPolicy>
		</broker>
    	 */
    	
    	Destination activeMQDlqDest = session.createQueue("ActiveMQ.DLQ");
    	MessageConsumer dlqConsumer = session.createConsumer(activeMQDlqDest);
    	dlqConsumer.setMessageListener(new MessageListener(){
			@Override
			public void onMessage(Message message) {
				System.out.println("DLQ:" + message);
			}});
    	//conn.close();
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
