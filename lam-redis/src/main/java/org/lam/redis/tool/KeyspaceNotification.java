package org.lam.redis.tool;

import org.lam.redis.client.RedisClient;

import lam.util.DateUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/**
* <p>
* key space notification
* </p>
* @author linanmiao
* @date 2017年2月5日
* @versio 1.0
*/
public class KeyspaceNotification {

	/**
	 * redis.conf
	 * notify-keyspace-events Ex
	 */
	public static void main(String[] args){
		RedisClient client = new RedisClient("192.168.20.112", 6379, null);
		Jedis jedis = client.getResource();
		jedis.subscribe(new JedisPubSub(){
			@Override
			public void onMessage(String channel, String message) {
				String str = String.format("time:%s\nchannel:%s\nmessage:%s", DateUtil.getCurrentTimeSSS(), channel, message);
				System.out.println(str);
			}
		}, "__keyevent@0__:expired");
		jedis.close();
		client.close();
	}
	
}
