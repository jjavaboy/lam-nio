package lam.mq.service.impl;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import com.google.gson.Gson;

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
	
	private static Gson gson = new Gson();

	@Override
	public boolean sendQueue(MQessage mqessage) {
		try {
			Connection conn = ActiveMQHolder.getInstance().getConnection();
			Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination dest = session.createQueue(mqessage.getName());
			MessageProducer producer = session.createProducer(dest);
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			Message message = session.createTextMessage(gson.toJson(mqessage));
			conn.start();
			producer.send(message);
			conn.close();
			return true;
		} catch (JMSException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean sendTopic(MQessage mqessage) {
		Connection conn;
		try {
			conn = ActiveMQHolder.getInstance().getConnection();
			Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination desc = session.createTopic(mqessage.getName());
			MessageProducer producer = session.createProducer(desc);
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			Message message = session.createTextMessage(gson.toJson(mqessage));
			conn.start();
			producer.send(message);
			conn.close();
			return true;
		} catch (JMSException e) {
			e.printStackTrace();
		}
		return false;
	}

}
