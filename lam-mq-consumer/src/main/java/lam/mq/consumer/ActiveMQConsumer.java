package lam.mq.consumer;

import java.io.Closeable;
import java.io.IOException;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import lam.dubbo.bankb.transfer.model.Transfer;
import lam.dubbo.bankb.transfer.service.TransferService;
import lam.mq.consumer.util.ActiveMQHolder;
import lam.mq.model.MQessage;
import lam.util.support.Startable;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年8月4日
* @version 1.0
*/
public class ActiveMQConsumer implements Startable, Closeable{
	
	private static Logger logger = LoggerFactory.getLogger(ActiveMQConsumer.class);
	
	private static Gson gson = new Gson();

	private static final String DEFAULT_QUEUE_NAME = Transfer.class.getSimpleName();
	
	private String queueName;
	
	private Connection conn;
	
	private TransferService transferService;
	
	public ActiveMQConsumer(){
		this(DEFAULT_QUEUE_NAME);
	}
	
	public ActiveMQConsumer(String queueName){
		this.queueName = queueName;
	}
	
	public void setTransferService(TransferService transferService) {
		this.transferService = transferService;
	}
	
	@Override
	public void close() throws IOException {
		logger.info("ActiveMQ consumer close");
		if(conn != null){
			try {
				conn.close();
			} catch (JMSException e) {
				logger.error("close connection error", e);
			}
		}
	}

	@Override
	public void start() {
		logger.info("ActiveMQ consumer start");
		try {
			conn = ActiveMQHolder.getInstance().getConnection();
			Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination dest = session.createQueue(queueName);
			MessageConsumer consumer = session.createConsumer(dest);
			conn.start();
			while(true){
				TextMessage message = (TextMessage) consumer.receive();
				MQessage mqessage = gson.fromJson(message.getText(), MQessage.class);
				Transfer transfer = gson.fromJson(mqessage.getText(), Transfer.class);
				transfer.setMessageId(message.getJMSMessageID());
				boolean result = transferService.doTransfer(transfer);
				logger.info(String.format("consume, message:%s, result:%b", gson.toJson(message), result));
			}
		} catch (Throwable e) {
			logger.error("ActiveMQ consumer error", e);
		}
	}

}
