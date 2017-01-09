package org.lam.redis.test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import lam.util.DateUtil;
import redis.clients.jedis.HostAndPort;
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
    	initializeSentinels(sentinels);
    	
    	JedisSentinelPool pool = new JedisSentinelPool("mymaster", sentinels, config);
    	
    	Jedis jedis = null;
    	try{
    		//返回master redis的连接，
    		//如果master down掉之后，sentinel进行故障转移，slave被提升为master，则进行返回master(即为之前的slave)
    		jedis = pool.getResource();
	    	String reply = jedis.set("mykey", "myvalue");
    		
    		System.out.println(DateUtil.getCurrentTimeSSS() + ":" + reply);
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
	    	//@deprecated starting from Jedis 3.0 this method will not be exposed.
	    	//pool.returnResource(jedis);
	    	
	    	if(jedis != null){
	    		jedis.close();
	    	}
    	}
    	pool.close();
    	
    }
    
    private static void initializeSentinels(Set<String> sentinels){
    	//Notice: sentinel server config, neither master nor slave redis config. 
    	sentinels.add(new HostAndPort("192.168.20.111", 26379).toString());//toString():192.168.204.127:26379
    }
    
    private static void sleep(){
    	try {
			TimeUnit.MILLISECONDS.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
}
