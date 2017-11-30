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
	
	private String host = Protocol.DEFAULT_HOST;
	
	private int port = Protocol.DEFAULT_PORT;
	
	private String password;
	
	private int timeout = 5000;//2000:Protocol.DEFAULT_TIMEOUT;
	
	private int maxTotal = GenericObjectPoolConfig.DEFAULT_MAX_TOTAL;
	
	private int maxIdle = GenericObjectPoolConfig.DEFAULT_MAX_IDLE;
	
	private int minIdle = GenericObjectPoolConfig.DEFAULT_MIN_IDLE;
	
	public RedisClient(){
		init();
	}
	
	public RedisClient(String host, int port, String password){
		this.host = host;
		this.port = port;
		this.password = password;
		init();
	}
	
	public RedisClient(String host, int port, String password, int maxTotal, int maxIdle, int minIdle) {
		this.host = host;
		this.port = port;
		this.password = password;
		this.maxTotal = maxTotal;
		this.maxIdle = maxIdle;
		this.minIdle = minIdle;
		init();
	}
	
	private void init(){
		final GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		poolConfig.setMaxTotal(maxTotal);
		poolConfig.setMaxIdle(maxIdle);
		poolConfig.setMinIdle(minIdle);
		poolConfig.setMaxWaitMillis(60000);//(millisecond)wait for one minute
		poolConfig.setBlockWhenExhausted(true);
		poolConfig.setTestOnBorrow(true);
		//You can also configurate other arguments for poolConfig.
		
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
