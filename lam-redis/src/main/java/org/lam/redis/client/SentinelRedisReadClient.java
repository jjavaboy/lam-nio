package org.lam.redis.client;

import java.io.Closeable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.lam.redis.JedisSentinelReadPool;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;

/**
* <p>
* sentinel redis client(read), it means you get slave redis.
* </p>
* @author linanmiao
* @date 2017年1月10日
* @versio 1.0
*/
public class SentinelRedisReadClient implements Closeable{
	
	private GenericObjectPoolConfig config;
	private JedisSentinelReadPool pool;

	private Set<String> sentinels;
	private String masterName;
	
	public SentinelRedisReadClient(){
		init();
	}
	
	private void init(){
		sentinels = new HashSet<String>();
		sentinels.add(new HostAndPort("192.168.20.111", 26379).toString());
		
		config = new GenericObjectPoolConfig();
		
		this.masterName = toMasterName(masterName);
		pool = new JedisSentinelReadPool(this.masterName, sentinels, config);
	}

	protected String toMasterName(String masterName){
		return StringUtils.isBlank(masterName) ? "mymaster" : masterName;
	}
	
	public void close(){
		if(pool != null){			
			pool.destroy();
		}
	}
	
	public Jedis getResource(){
		return pool.getResource();
	}
	
	public void close(Jedis jedis){
		if(jedis != null){
			jedis.close();
		}
	}

}
