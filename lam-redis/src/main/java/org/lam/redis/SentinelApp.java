package org.lam.redis;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

/**
 * Hello world!
 *
 */
public class SentinelApp {
    public static void main( String[] args ){
    	GenericObjectPoolConfig config = new GenericObjectPoolConfig();
    	config.setMaxIdle(GenericObjectPoolConfig.DEFAULT_MAX_IDLE);
    	config.setMinIdle(GenericObjectPoolConfig.DEFAULT_MIN_IDLE);
    	config.setMaxTotal(GenericObjectPoolConfig.DEFAULT_MAX_TOTAL);
    	//...other arguments for config
    	
    	Set<String> sentinels = new HashSet<>();
    	
    	JedisSentinelPool pool = new JedisSentinelPool("mymaster", sentinels, config);
    	
    	Jedis jedis = pool.getResource();
    	try{
	    	//handle business with jedis
	    	//...
    	}finally{
	    	//@deprecated starting from Jedis 3.0 this method will not be exposed.
	    	//pool.returnResource(jedis);
	    	
	    	jedis.close();
    	}
    	
    	pool.close();
    }
}
