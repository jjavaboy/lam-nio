package org.lam.hongbao.service.concurrent;

import org.lam.redis.client.RedisClient;

import redis.clients.jedis.Jedis;

/**
 * <p>
 * 使用redis的sexnx操作获得锁<br/>
 * 实现分布式锁执行安全<br/>
 * 如果在获得锁失败的情况下想多次尝试执行操作，可以通过构造函数传入重试次数(retry，默认是0)<br/>
 * </p>
 * 
 * @author linanmiao
 * @date 2017年1月20日
 * @version 1.0
 */
public abstract class DistributedExecutor {

	private static RedisClient client = new RedisClient("", 6378, null);

	public DistributedExecutor(String key){
		this.key = key;
	}
	
	public DistributedExecutor(String key, String value) {
		this(key);
		this.value = value;
	}

	public DistributedExecutor(int retry, String key, String value) {
		this(key, value);
		this.retry = retry;
	}
	
	public DistributedExecutor(int retry, String key, String value, int expireSeconds){
		this(retry, key, value);
		this.expireSeconds = expireSeconds;
	}

	public abstract boolean execute();
	
	public boolean run() {
		Jedis jedis = client.getResource();
		try {
			while (retry-- >= 0) {
				try{
				if (jedis.setnx(getKey(), getValue()) == 1) {
					jedis.expire(getKey(), expireSeconds);
					return execute();
				}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			return false;
		} finally {
			if(jedis != null){
				jedis.del(getKey());
				jedis.close();
			}
		}
	}
	
	private int retry = 0;
	
	private final String key;
	
	private String value = "1";
	
	private int expireSeconds = 60 * 30; //half of an hour;

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

}
