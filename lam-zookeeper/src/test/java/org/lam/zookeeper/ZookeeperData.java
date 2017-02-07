package org.lam.zookeeper;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import lam.log.Console;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年2月7日
* @versio 1.0
*/
public class ZookeeperData implements Watcher{
	
	private static CountDownLatch connectLatch = new CountDownLatch(1);
	private static ZooKeeper zk;
	
	public static void main(String[] args) throws IOException, InterruptedException, KeeperException{
		zk = new ZooKeeper("192.168.20.111:2181", 5000, new ZookeeperData());
		connectLatch.await();
		
		String path = "/mynode";
		Stat stat = new Stat();
		byte[] bytes = zk.getData(path, true, stat);
		Console.println(new String(bytes));
		
		Stat stat2 = zk.setData(path, "{\"service\":\"127.0.0.1:8081\"}".getBytes(), stat.getVersion());
		Console.println(stat2);
		
		byte[] bytes2 = zk.getData(path, true, stat2);
		Console.println(new String(bytes2));
		
		sleep(Integer.MAX_VALUE);
	}
	
	@Override
	public void process(WatchedEvent event) {
		Console.println(event);
		try{
		if(event.getState() == Watcher.Event.KeeperState.SyncConnected){
			if(event.getType() == Watcher.Event.EventType.None && event.getPath() == null){
				connectLatch.countDown();
			}else if(event.getType() == Watcher.Event.EventType.NodeChildrenChanged){
				List<String> list = zk.getChildren(event.getPath(), true);
				Console.println("node %s children changed, new children:%s", event.getPath(), list.toString());
			}else if(event.getType() == Watcher.Event.EventType.NodeCreated){
				Stat stat = new Stat();
				byte[] bytes = zk.getData(event.getPath(), true, stat);
				Console.println("node %s created, data:%s, stat:%s", event.getPath(), new String(bytes), stat);
			}else if(event.getType() == Watcher.Event.EventType.NodeDataChanged){
				Stat stat = new Stat();
				byte[] bytes = zk.getData(event.getPath(), true, stat);
				Console.println("node %s changed, data:%s, stat:%s", event.getPath(), new String(bytes), stat);
			}else if(event.getType() == Watcher.Event.EventType.NodeDeleted){
				Console.println("node %s deleted", event.getPath());
			}
		}
		}catch(Exception e){
			Console.error(e);
		}
	}
	
	private static void sleep(int timeout){
		try {
			TimeUnit.SECONDS.sleep(timeout);
		} catch (InterruptedException e) {
			Console.error(e);
		}
	}

}
