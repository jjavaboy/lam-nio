package org.lam.redis.client;

import java.io.Closeable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

/**
* <p>
* sentinel client
* </p>
* @author linanmiao
* @date 2017年1月9日
* @versio 1.0
*/
public class SentinelRedisClient implements Closeable{
	
	protected GenericObjectPoolConfig config;
	
	protected JedisSentinelPool pool;
	
	protected Set<String> sentinels;
	
	protected String masterName;

	protected SentinelRedisClient(){
		initSentinels();
	}
	
	protected void initSentinels(){
		config = new GenericObjectPoolConfig();
    	config.setMaxIdle(GenericObjectPoolConfig.DEFAULT_MAX_IDLE);
    	config.setMinIdle(GenericObjectPoolConfig.DEFAULT_MIN_IDLE);
    	config.setMaxTotal(GenericObjectPoolConfig.DEFAULT_MAX_TOTAL);
    	//...other arguments for config
    	
    	sentinels = new HashSet<String>();
    	sentinels.add(new HostAndPort("192.168.20.111", 26379).toString());
    	
    	masterName = toMasterName(masterName);
    	pool = new JedisSentinelPool(masterName, sentinels, config);
	}
	
	protected String toMasterName(String masterName){
		return StringUtils.isBlank(masterName) ? "mymaster" : masterName;
	}
	
	public Jedis getJedis(){
		return pool.getResource();
	}
	
	public void close(Jedis jedis){
		if(jedis != null){
			//deprecated
			//pool.returnResource(jedis);
			jedis.close();
		}
	}

	@Override
	public void close(){
		if(pool != null){
			pool.destroy();
		}
	}

}
