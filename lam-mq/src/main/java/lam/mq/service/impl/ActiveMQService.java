package lam.mq.service.impl;

import java.io.Serializable;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.protobuf.Type;

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
	
	/*public static class PointAdapter extends TypeAdapter {
	     public Point read(JsonReader reader) throws IOException {
	       if (reader.peek() == JsonToken.NULL) {
	         reader.nextNull();
	         return null;
	       }
	       String xy = reader.nextString();
	       String[] parts = xy.split(",");
	       int x = Integer.parseInt(parts[0]);
	       int y = Integer.parseInt(parts[1]);
	       return new Point(x, y);
	     }
	     public void write(JsonWriter writer, Point value) throws IOException {
	       if (value == null) {
	         writer.nullValue();
	         return;
	       }
	       String xy = value.getX() + "," + value.getY();
	       writer.value(xy);
	     }
	   }}*/
	
	private static Logger logger = LoggerFactory.getLogger(ActiveMQService.class);
	
	private static Gson gson = /*new GsonBuilder().registerTypeAdapter(Serializable.class, typeAdapter)
			
			.registerTypeAdapterFactory(new TypeAdapterFactory(){
		@Override
		public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
			// TODO Auto-generated method stub
			return null;
		}}).create();*/ new Gson();

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
			Connection conn = ActiveMQHolder.getInstance().getConnection();
			Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination desc = session.createTopic(mqessage.getName());
			MessageProducer producer = session.createProducer(desc);
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			Message message = session.createTextMessage(gson.toJson(mqessage));
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
