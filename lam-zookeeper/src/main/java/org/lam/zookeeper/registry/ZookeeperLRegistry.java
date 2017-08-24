package org.lam.zookeeper.registry;

import java.io.Closeable;
import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lam.distribution.LRegistry;
import lam.util.NetUtils;
import lam.util.Strings;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年8月24日
* @version 1.0
*/
public class ZookeeperLRegistry implements LRegistry, Closeable{
	
	private final String parentPath;
	
	private final String subPath;
	
	private ZkClient zkClient;
	
	private Logger logger = LoggerFactory.getLogger(ZookeeperLRegistry.class);
	
	public ZookeeperLRegistry(String parentPath, String subPath){
		this.parentPath = parentPath;
		this.subPath = subPath;
		this.zkClient = ZkHolder.getInstance().createZkClient();
	}

	/**
	 * 创建zk路径，创建成功，则获得队列的消费权
	 */
	@Override
	public boolean doRegistry() {
		String ip = NetUtils.getLocalHost();
		String path = parentPath + subPath;
		boolean nodeExists = false;
		logger.info("create path:" + path + " with data:" + ip);
		try{
			path = zkClient.create(path, ip, CreateMode.EPHEMERAL);
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
	@Override
	public void subcribe(final NotifyListener notify) {
		zkClient.subscribeChildChanges(parentPath, new IZkChildListener(){
			@Override
			public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
				logger.info(String.format("child change: parentPath:%s , childs:%s", parentPath, currentChilds));
				if(Strings.isNullOrEmpty(currentChilds)){
					boolean result = doRegistry();
					if(result){
						notify.listen();
					}
				}
			}});
	}
	
	@Override
	public void close(){
		if(zkClient != null){
			zkClient.close();
			zkClient = null;
			logger.info("zkClient closed.");
		}
	}

}
