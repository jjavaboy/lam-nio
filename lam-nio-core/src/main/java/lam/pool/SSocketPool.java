package lam.pool;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Protocol;

/**
* <p>
* socket Pool
* </p>
* @author linanmiao
* @date 2017年3月21日
* @version 1.0
*/
public class SSocketPool extends SPool<SSocket> implements Pooling<SSocket>{

	public SSocketPool(GenericObjectPoolConfig config, PooledObjectFactory<SSocket> factory){
		super(config, factory);
	}

	@Override
	public SSocket getResource() {
		SSocket s = super.getResource();
		return s;
	}

	@Override
	public void returnResource(SSocket t) {
		if(t != null){
			super.returnResource(t);
		}
	}

	@Override
	public void returnBrokenResource(SSocket t) {
		if(t != null){
			super.returnBrokenResource(t);
		}
	}
	
	public static void main(String[] args){
		JedisPool jedisPool = new JedisPool(Protocol.DEFAULT_HOST, Protocol.DEFAULT_PORT);
		Jedis jedis = jedisPool.getResource();
		jedisPool.returnResource(jedis);
		//jedis.close();
		jedisPool.close();
	}

}
