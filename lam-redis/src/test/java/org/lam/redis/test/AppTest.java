package org.lam.redis.test;

import org.junit.Test;
import org.lam.redis.model.SNode;

import redis.clients.jedis.Jedis;

/**
* <p>
* App Test class
* </p>
* @author linanmiao
* @date 2017年1月12日
* @version 1.0
*/
public class AppTest {
	
	@Test
	public void slaveRedis(){
		Jedis slave = new Jedis("192.168.204.127", 6378);
		String reply = slave.info("replication");
		System.out.println(reply);
		String[] replyArray = reply.split("\n");
		for(int idx = 0; idx < replyArray.length; idx++){
			if(replyArray[idx].indexOf(':') != -1){
			System.out.print(idx + ")" + 
			SNode.parse(replyArray[idx].replace("\r", ""), ':'));
			}
		}
		slave.close();
	}
	
	public static void main(String[] args){
		String key1 = null;
		String key2 = null;
		System.out.println(key1 == key2);
	}
	
}
