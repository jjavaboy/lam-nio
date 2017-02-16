package org.lam.redis.test;

import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年2月16日
* @version 1.0
*/
public class ShardedJedisTest {
	
	public static void main(String[] args){
		HostAndPort hap1 = new HostAndPort("192.168.204.127", 6378);
		HostAndPort hap2 = new HostAndPort("192.168.204.127", 6379);
		
		//constructor arguments:host, port, connectionTimeout, socketTimeout, weight
		//JedisShardInfo info = new JedisShardInfo(hap1.getHost(), hap1.getPort(), 2000, 2000, 1);
		JedisShardInfo info1 = new JedisShardInfo(hap1.getHost(), hap1.getPort());
		//info1.setPassword(null);
		JedisShardInfo info2 = new JedisShardInfo(hap2.getHost(), hap2.getPort());
		List<JedisShardInfo> list = new ArrayList<JedisShardInfo>();
		list.add(info1);
		list.add(info2);
		
		ShardedJedis shardedJedis = new ShardedJedis(list);
		String replya = shardedJedis.set("a", "avalue");
		System.out.println("key a, reply:" + replya + ", shard info:" + shardedJedis.getShardInfo("a"));
		
		String replyb = shardedJedis.set("b", "bvalue");
		System.out.println("key b, reply:" + replyb + ", shard info:" + shardedJedis.getShardInfo("b"));
		
	    shardedJedis.close();
	}

}
