package lam.mq.service.impl;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.broker.region.policy.RedeliveryPolicyMap;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import lam.dubbo.bankb.transfer.model.Transfer;
import lam.mq.model.MQessage;
import lam.mq.service.MQService;
import lam.mq.service.impl.util.ActiveMQHolder;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年8月3日
* @version 1.0
*/
public class ActiveMQService implements MQService{
	
	private static Logger logger = LoggerFactory.getLogger(ActiveMQService.class);
	
	private static Gson gson = new Gson();
	
	private ActiveMQConnection conn;
	
	public ActiveMQService(){
		try {
			//文档参考http://activemq.apache.org/redelivery-policy.html
			conn = ActiveMQHolder.getInstance().createConnection();
			
			RedeliveryPolicy queuePolicy = new RedeliveryPolicy();
			queuePolicy.setInitialRedeliveryDelay(0);
			queuePolicy.setRedeliveryDelay(1000);
			queuePolicy.setUseExponentialBackOff(false);
			queuePolicy.setMaximumRedeliveries(6);
			
			//RedeliveryPolicy topicPolicy = new RedeliveryPolicy();
			
			RedeliveryPolicyMap redeliveryPolicyMap = new RedeliveryPolicyMap();
			redeliveryPolicyMap.put(new ActiveMQQueue(Transfer.class.getSimpleName()), queuePolicy);
			conn.setRedeliveryPolicyMap(redeliveryPolicyMap);
		} catch (JMSException e) {
			throw new RuntimeException("create ActiveMQConnection error", e);
		}
	}

	@Override
	public boolean sendQueue(MQessage mqessage) {
		if(!mqessage.isQueue())
			return false;
		boolean result = false;
		try {
			Connection conn = ActiveMQHolder.getInstance().getConnection();
			Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination dest = session.createQueue(mqessage.getName());
			MessageProducer producer = session.createProducer(dest);
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			Message message = session.createTextMessage(gson.toJson(mqessage));
			message.setJMSRedelivered(true);
			conn.start();
			producer.send(message);
			conn.close();
			result = true;
		} catch (JMSException e) {
			logger.error("sendQueue error", e);
		}
		logger.info(String.format("sendQueue:%s, result:%b", gson.toJson(mqessage), result));
		return result;
	}

	@Override
	public boolean sendTopic(MQessage mqessage) {
		if(!mqessage.isTopic())
			return false;
		boolean result = false;
		try {
			Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination desc = session.createTopic(mqessage.getName());
			MessageProducer producer = session.createProducer(desc);
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			Message message = session.createTextMessage(gson.toJson(mqessage));
			message.setJMSRedelivered(true);
			
			conn.start();
			producer.send(message);
			conn.close();
			result = true;
		} catch (JMSException e) {
			logger.error("sendTopic error", e);
		}
		logger.info(String.format("%s, result:%b", gson.toJson(mqessage), result));
		return result;
	}

}
