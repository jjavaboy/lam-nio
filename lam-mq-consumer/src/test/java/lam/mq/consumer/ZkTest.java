package lam.mq.consumer;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.apache.zookeeper.CreateMode;

import lam.util.Strings;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年8月22日
* @version 1.0
*/
public class ZkTest {
	
	public static void main(String[] args){
		final String ip = args[0];
		final ZkClient zkClient = new ZkClient(buildDefaultZkConnection(), Integer.MAX_VALUE, buildDefaultZkSerializer());
		final String parentPath = "/zk_test/consumer";
		
		tryCreatePath(parentPath, zkClient, ip);
		subscribeChildChange(parentPath, zkClient, ip);
		
		synchronized (ZkTest.class) {
			try {
				ZkTest.class.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void subscribeChildChange(String parentPath, final ZkClient zkClient, final String ip){
		zkClient.subscribeChildChanges(parentPath, new IZkChildListener(){
			@Override
			public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
				System.out.println(String.format("child change: parentPath:%s , childs:%s", parentPath, currentChilds));
				if(Strings.isNullOrEmpty(currentChilds)){
					tryCreatePath(parentPath, zkClient, ip);
				}
			}});
		
	}
	
	private static boolean tryCreatePath(String parentPath, ZkClient zkClient, String ip){
		String path = parentPath + "/ActiveMQ";
		boolean nodeExists = false;
		System.out.println("create path:" + path + " with data:" + ip);
		try{
			path = zkClient.create(path, ip, CreateMode.EPHEMERAL);
		}catch(Exception e){
			if(e instanceof ZkNodeExistsException){
				nodeExists = true;
			}
		}
		if(!nodeExists){			
			System.out.println("path: " + path + " not exists, create successful");
		}else{
			boolean returnNullIfPathNotExists = true;
			String data = zkClient.readData(path, returnNullIfPathNotExists);
			System.out.println("path: " + path + " has exists, create failed, data:" + data);
		}
		return !nodeExists;
	}
	
	private static ZkConnection buildDefaultZkConnection(){
		String zkServers = "192.168.204.79:2181";
		int sessionTimeOut = 30000;
		ZkConnection zkConnection = new ZkConnection(zkServers, sessionTimeOut);
		return zkConnection;
	}
	
	private static ZkSerializer buildDefaultZkSerializer(){
		ZkSerializer zkSerializer = new ZkSerializer() {
			String charsetName = "utf-8";
			@Override
			public byte[] serialize(Object data) throws ZkMarshallingError {
				try {
					return ((String)data).getBytes(charsetName);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					return null;
				}
			}
			
			@Override
			public Object deserialize(byte[] bytes) throws ZkMarshallingError {
				try {
					return new String(bytes, charsetName);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					return null;
				}
			}
		};
		return zkSerializer;
	}

}
