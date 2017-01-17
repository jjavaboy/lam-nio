package org.lam.redis.client;

import java.io.Closeable;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Protocol;

/**
* <p>
* redis client
* </p>
* @author linanmiao
* @date 2017年1月17日
* @versio 1.0
*/
public class RedisClient implements Closeable{
	
	private JedisPool pool;
	
	private String host;
	
	private int port;
	
	private String password;
	
	private int timeout = 5000;//2000:Protocol.DEFAULT_TIMEOUT;
	
	public RedisClient(){
		init();
	}
	
	private void init(){
		final GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		poolConfig.setMaxTotal(GenericObjectPoolConfig.DEFAULT_MAX_TOTAL);
		poolConfig.setMaxIdle(GenericObjectPoolConfig.DEFAULT_MAX_IDLE);
		poolConfig.setMinIdle(GenericObjectPoolConfig.DEFAULT_MIN_IDLE);
		//You can also configurate other arguments for poolConfig.
		
		host = "127.0.0.1";
		port = 6379;
		
		pool = new JedisPool(poolConfig, host, port, timeout, password, Protocol.DEFAULT_DATABASE);
	}
	
	public Jedis getResource(){
		return pool.getResource();
	}
	
	public void close(Jedis jedis){
		if(jedis != null){
			jedis.close();
		}
	}
	
	public void close(){
		if(pool != null){
			pool.close();
		}
	}

}
