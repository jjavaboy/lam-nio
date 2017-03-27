package lam.pool;

import lam.pool.support.SObjectPoolConfig;
import lam.pool.support.SPooledObjectFactory;
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

	public SSocketPool(SObjectPoolConfig config, SPooledObjectFactory<SSocket> factory){
		super(config, factory);
	}
	
	@Override
	public SSocket getResource() {
		SSocket s = super.getResource();
		if(s != null){
			s.setDataSource(this);
		}
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
		/*JedisPool jedisPool = new JedisPool(Protocol.DEFAULT_HOST, Protocol.DEFAULT_PORT);
		Jedis jedis = jedisPool.getResource();
		jedis.close();
		jedisPool.close();*/
		SObjectPoolConfig config = new SObjectPoolConfig();
		SSocketPool ssocketPool = new SSocketPool(config, new SSocketFactory("192.168.204.127", 6378));
		SSocket ssocket = ssocketPool.getResource();
		ssocket.close();
		ssocketPool.close();
	}

}
