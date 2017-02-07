package org.lam.zookeeper;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.google.gson.Gson;

import lam.util.DateUtil;

public class ZookeeperWatcher implements Watcher{

	private static CountDownLatch latch = new CountDownLatch(1);
	private static ZooKeeper zookeeper;
	private static Gson gson = new Gson();
	
	@Override
	public void process(WatchedEvent event) {
		System.out.println(String.format("%s:received WatchedEvent:[%s]", DateUtil.getCurrentTimeSSS(), event.toString()));
		if(event.getState() == KeeperState.SyncConnected){
			if(event.getType() == EventType.None && event.getPath() == null){
				latch.countDown();
			}else if(event.getType() == EventType.NodeChildrenChanged){
				try {
					System.out.println("node children changed, get children again\n" + zookeeper.getChildren(event.getPath(), true));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args){
		try {
			zookeeper = new ZooKeeper("192.168.204.127:2181", 3000, new ZookeeperWatcher());
			latch.await();
			System.out.println(String.format("%s:client has connected to the zookeeper server, state:[%s], sessionId:%d, sessionPasswd:%s",
					DateUtil.getCurrentTimeSSS(), zookeeper.getState(), zookeeper.getSessionId(), new String(zookeeper.getSessionPasswd())));
			
			/*String path1 = zookeeper.create("/zk_test/node1", "123".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			String path2 = zookeeper.create("/zk_test/node2", "123456".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
			sleep(30);
			
			zookeeper.create("/zk_test/asynnode1", "111".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, 
					new IStringCallback(), "I am context.");
			sleep(60);*/
			
			/*List<String> list = zookeeper.getChildren("/", true);
			System.out.println("list:\n" + list);*/
			
			zookeeper.getChildren("/", true, new IChildren2Callback(), null);
			
			zookeeper.create("/zk_fuu", "foo".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			
			final Stat stat = new Stat();
			Watcher watcher = new Watcher(){
				@Override
				public void process(WatchedEvent event) {
					if(event.getState() == KeeperState.SyncConnected){
						if(event.getType() == EventType.None && event.getPath() == null){
							System.out.println("connected");
						}else if(event.getType() == EventType.NodeDataChanged){
							try {
								byte[] d = zookeeper.getData(event.getPath(), this, stat);
								System.out.println(String.format("node %s data changed, getData again data:%s, stat:%s", 
										event.getPath(), new String(d), gson.toJson(stat)));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}};
			byte[] data = zookeeper.getData("/zk_foo", watcher, stat);
			System.out.println(String.format("data:%s,stat:%s" , new String(data), gson.toJson(stat)));
			
			sleep(60);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void sleep(long timeout){
		try {
			TimeUnit.SECONDS.sleep(timeout);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private static class IStringCallback implements AsyncCallback.StringCallback{

		@Override
		public void processResult(int rc, String path, Object ctx, String name) {
			System.out.println(String.format("path create result:%d, %s, %s, %s", rc, path, ctx, name));
		}
		
	}
	
	private static class IChildren2Callback implements AsyncCallback.Children2Callback{

		@Override
		public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
			System.out.println(String.format("getChildren result:%d, %s, %s, %s, stat:%s", rc, path, ctx, children, stat));
		}
		
	}
	
}
