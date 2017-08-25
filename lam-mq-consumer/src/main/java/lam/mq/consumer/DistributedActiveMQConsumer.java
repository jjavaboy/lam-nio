package lam.mq.consumer;

import java.io.Closeable;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.lam.zookeeper.registry.ZookeeperLRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import lam.distribution.LRegistry;
import lam.mq.consumer.util.ActiveMQHolder;
import lam.util.FinalizeUtils;
import lam.util.support.Startable;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年8月22日
* @version 1.0
*/
public class DistributedActiveMQConsumer implements LRegistry.NotifyListener, Startable, Closeable{
	
	private static Logger logger = LoggerFactory.getLogger(DistributedActiveMQConsumer.class.getSimpleName());
	
	private static Gson gson = new Gson();
	
	private String queueName = "consumer_distribution";
	
	private LRegistry registry;
	
	private ActiveMQConnection conn;
	
	public DistributedActiveMQConsumer(){
		try {
			conn = ActiveMQHolder.getInstance().createConnection();
			registry = new ZookeeperLRegistry("/zk_test/consumer", "/ActiveMQ");
		} catch (JMSException e) {
			logger.error("ActiveMQ create connection error", e);
		}
	}

	@Override
	public void close() {
		if(conn != null){
			try {
				conn.close();
				logger.info("ActiveMQConnection closed");
			} catch (JMSException e) {
				logger.error("close connection of ActiveMQ error", e);
			}
		}
		if(registry != null){
			FinalizeUtils.closeNotQuietly(registry);
		}
	}

	@Override
	public void start() {
		boolean success = registry.doRegistry();
		if(success){
			listen();
		}
		registry.subcribe(this);
	}
	
	@Override
	public void listen(){
		logger.info("start to listen on queue name:" + queueName);
		try {
			Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination dest = session.createQueue(queueName);
			MessageConsumer consumer = session.createConsumer(dest);
			conn.start();
			consumer.setMessageListener(new MessageListener(){
				@Override
				public void onMessage(Message message) {
					logger.info("onMessage: " + gson.toJson(message));
				}});
		} catch (JMSException e) {
			logger.error("ActiveMQ start to listen to queue " + queueName + " error.", e);
		}
	}

}
