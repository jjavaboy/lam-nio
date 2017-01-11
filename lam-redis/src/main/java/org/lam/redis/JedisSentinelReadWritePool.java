package org.lam.redis;

import java.util.Set;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.Protocol;
import redis.clients.util.Pool;

/**
* <p>
* Contains jedis(read/write) pool 
* It should be read pool only, not read and write pool.
* This class should extends Pool<T>, not JedisSentinelPool.
* </p>
* @author linanmiao
* @date 2017年1月10日
* @versio 1.0
*/
public class JedisSentinelReadWritePool extends JedisSentinelPool{
	
	private ReadPool<Jedis> readPool;
	
	private volatile HostAndPort currentHostSlave;
	private volatile JedisReadFactory jedisReadFactory;
	
	public JedisSentinelReadWritePool(String masterName, Set<String> sentinels){
		this(masterName, sentinels, new GenericObjectPoolConfig());
	}
	
	public JedisSentinelReadWritePool(String masterName, Set<String> sentinels,
		      final GenericObjectPoolConfig poolConfig){
		this(masterName, sentinels, poolConfig, Protocol.DEFAULT_TIMEOUT, null, Protocol.DEFAULT_DATABASE, null);
	}
	
	public JedisSentinelReadWritePool(String masterName, Set<String> sentinels, String password){
		this(masterName, sentinels, new GenericObjectPoolConfig(), Protocol.DEFAULT_TIMEOUT, password,
				Protocol.DEFAULT_DATABASE, null);
	}
	
	public JedisSentinelReadWritePool(String masterName, Set<String> sentinels,
      final GenericObjectPoolConfig poolConfig, int timeout, final String password,
      final int database, final String clientName){
		this(masterName, sentinels, poolConfig, timeout, timeout, password, database, clientName);
	}
	
	public JedisSentinelReadWritePool(String masterName, Set<String> sentinels,
      final GenericObjectPoolConfig poolConfig, final int timeout, final int soTimeout,
      final String password, final int database){
		this(masterName, sentinels, poolConfig, timeout, soTimeout, password, database, null);
	}
	
	public JedisSentinelReadWritePool(String masterName, Set<String> sentinels,
			final GenericObjectPoolConfig poolConfig, final int connectionTimeout, final int soTimeout,
			final String password, final int database, final String clientName){
		super(masterName, sentinels, poolConfig, connectionTimeout, soTimeout, password, database, clientName);
		readPool = new ReadPool<Jedis>();
		HostAndPort currentHostMaster = getCurrentHostMaster();
		initReadPool(currentHostMaster);
	}

	private void initReadPool(HostAndPort currentHostMaster) {
		if(currentHostSlave == null){
			currentHostSlave = initCurrentHostSlave(currentHostMaster);
		}
		if(jedisReadFactory == null){
			jedisReadFactory = new JedisReadFactory(currentHostSlave.getHost(), currentHostSlave.getPort(),
					connectionTimeout, soTimeout, password, database, clientName, false, null, null, null);
			readPool.initPool(poolConfig, jedisReadFactory);
		}else{
			jedisReadFactory.setHostAndPort(currentHostSlave);
			readPool.getInternalPool().clear();
		}
		log.info("Created JedisReadPool to Slave at " + currentHostSlave);
	}
	
	private HostAndPort initCurrentHostSlave(HostAndPort currentHostMaster){
		//get slave host according to master host
		Jedis master = new Jedis(currentHostMaster.getHost(), currentHostMaster.getPort());
		String reply = master.info();
		HostAndPort slave = null;
		return slave;
	}
	
	public Jedis getReadResource(){
		while(true){
			Jedis jedis = readPool.getResource();
			jedis.setDataSource(this);
			
			final HostAndPort slave = currentHostSlave;
			final HostAndPort slaveConnection = new HostAndPort(jedis.getClient().getHost(), 
					jedis.getClient().getPort());
			if(slave.equals(slaveConnection)){
				return jedis;
			}else{
				readPool.returnBrokenResource(jedis);
			}
		}
	}
	
	protected class ReadPool<T> extends Pool<T>{
		public ReadPool(){}
		
		public ReadPool(final GenericObjectPoolConfig poolConfig, PooledObjectFactory<T> factory){
			initPool(poolConfig, factory);
		}
		
		protected GenericObjectPool<T> getInternalPool(){
			return internalPool;
		}
	}
}
