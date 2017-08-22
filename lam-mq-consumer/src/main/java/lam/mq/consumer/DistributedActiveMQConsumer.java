package lam.mq.consumer;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.apache.activemq.ActiveMQConnection;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.common.utils.NetUtils;
import com.google.gson.Gson;

import lam.mq.consumer.util.ActiveMQHolder;
import lam.mq.consumer.util.ZkHolder;
import lam.util.Strings;
import lam.util.support.Startable;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年8月22日
* @version 1.0
*/
public class DistributedActiveMQConsumer implements Startable, Closeable{
	
	private static Logger logger = LoggerFactory.getLogger(DistributedActiveMQConsumer.class.getSimpleName());
	
	private static Gson gson = new Gson();
	
	//zookeeper父路径path
	private static final String ZK_PARENT_PATH = "/zk_test/consumer";
	
	//zookeeper消费者的path
	private static final String ZK_SUB_PATH = "/ActiveMQ";
	
	private String queueName = "consumer_distribution";
	
	private ZkClient zkClient;
	
	private ActiveMQConnection conn;
	
	public DistributedActiveMQConsumer(){
		try {
			zkClient = ZkHolder.getInstance().getZkClient();
			conn = ActiveMQHolder.getInstance().createConnection();
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
		if(zkClient != null){
			zkClient.close();
			logger.info("ZkClient closed");
		}
	}

	@Override
	public void start() {
		String ip = NetUtils.getLocalHost();
		boolean success = tryCreatePath(ZK_PARENT_PATH, ZK_SUB_PATH, ip);
		if(success){
			listen();
		}
		subscribeChildChange(ZK_PARENT_PATH, ZK_SUB_PATH, ip);
	}
	
	private void listen(){
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
	
	/**
	 * 创建zk路径，创建成功，则获得队列的消费权
	 */
	public boolean tryCreatePath(String parentPath, String subPath, String data){
		String path = parentPath + subPath;
		boolean nodeExists = false;
		logger.info("create path:" + path + " with data:" + data);
		try{
			path = zkClient.create(path, data, CreateMode.EPHEMERAL);
		}catch(Exception e){
			if(e instanceof ZkNodeExistsException){
				nodeExists = true;
			}
		}
		if(!nodeExists){			
			logger.info("path: " + path + " not exists, create successful");
		}else{
			boolean returnNullIfPathNotExists = true;
			String oldData = zkClient.readData(path, returnNullIfPathNotExists);
			logger.info("path: " + path + " has exists, create failed, data:" + oldData);
		}
		return !nodeExists;
	}
	
	/**
	 * 订阅父路径下的子列表，列表有变化，说明消费端可能是退出。
	 */
	public void subscribeChildChange(String parentPath, final String subPath, final String data){
		zkClient.subscribeChildChanges(parentPath, new IZkChildListener(){
			@Override
			public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
				logger.info(String.format("child change: parentPath:%s , childs:%s", parentPath, currentChilds));
				if(Strings.isNullOrEmpty(currentChilds)){
					boolean success = tryCreatePath(parentPath, subPath, data);
					if(success){
						listen();
					}
				}
			}});
		
	}

}
