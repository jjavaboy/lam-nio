package lam.concurrent.lock;

import java.util.Objects;

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

	private Jedis jedis;

	public DistributedExecutor(Jedis jedis, String key){
		Objects.requireNonNull(jedis, "parameter jedis cann't be null.");
		this.jedis = jedis;
		this.key = key;
	}
	
	public DistributedExecutor(Jedis jedis, String key, String value) {
		this(jedis, key);
		this.value = value;
	}

	public DistributedExecutor(Jedis jedis, int retry, String key, String value) {
		this(jedis, key, value);
		this.retry = retry;
	}
	
	public DistributedExecutor(Jedis jedis, int retry, String key, String value, int expireSeconds){
		this(jedis, retry, key, value);
		this.expireSeconds = expireSeconds;
	}

	public abstract boolean execute();
	
	public boolean run() {
		try {
			while (retry-- >= 0) {
				try{
					if ("OK".equals(jedis.set(getKey(), getValue(), "NX", "EX", expireSeconds))) {
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
			}
		}
	}
	
	private int retry = 0;
	
	private final String key;
	
	private String value = "1";
	
	private int expireSeconds = 60 * 30; //half of an hour by default;

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

}
