package org.lam.redis;

import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.Protocol;

/**
* <p>
* Contains jedis(read/write) pool
* </p>
* @author linanmiao
* @date 2017年1月10日
* @versio 1.0
*/
public class JedisSentinelReadWritePool extends JedisSentinelPool{

	  public JedisSentinelReadWritePool(String masterName, Set<String> sentinels) {
	    this(masterName, sentinels, new GenericObjectPoolConfig(), Protocol.DEFAULT_TIMEOUT, null,
	        Protocol.DEFAULT_DATABASE);
	  }

	  public JedisSentinelReadWritePool(String masterName, Set<String> sentinels, String password) {
	    this(masterName, sentinels, new GenericObjectPoolConfig(), Protocol.DEFAULT_TIMEOUT, password);
	  }

	  public JedisSentinelReadWritePool(String masterName, Set<String> sentinels,
	      final GenericObjectPoolConfig poolConfig, int timeout, final String password) {
	    this(masterName, sentinels, poolConfig, timeout, password, Protocol.DEFAULT_DATABASE);
	  }

	  public JedisSentinelReadWritePool(String masterName, Set<String> sentinels,
	      final GenericObjectPoolConfig poolConfig, final int timeout) {
	    this(masterName, sentinels, poolConfig, timeout, null, Protocol.DEFAULT_DATABASE);
	  }

	  public JedisSentinelReadWritePool(String masterName, Set<String> sentinels,
	      final GenericObjectPoolConfig poolConfig, final String password) {
	    this(masterName, sentinels, poolConfig, Protocol.DEFAULT_TIMEOUT, password);
	  }

	  public JedisSentinelReadWritePool(String masterName, Set<String> sentinels,
	      final GenericObjectPoolConfig poolConfig, int timeout, final String password,
	      final int database) {
	    this(masterName, sentinels, poolConfig, timeout, timeout, password, database);
	  }

	  public JedisSentinelReadWritePool(String masterName, Set<String> sentinels,
	      final GenericObjectPoolConfig poolConfig, int timeout, final String password,
	      final int database, final String clientName) {
	    this(masterName, sentinels, poolConfig, timeout, timeout, password, database, clientName);
	  }

	  public JedisSentinelReadWritePool(String masterName, Set<String> sentinels,
	      final GenericObjectPoolConfig poolConfig, final int timeout, final int soTimeout,
	      final String password, final int database) {
	    this(masterName, sentinels, poolConfig, timeout, soTimeout, password, database, null);
	  }

	  public JedisSentinelReadWritePool(String masterName, Set<String> sentinels,
	      final GenericObjectPoolConfig poolConfig, final int connectionTimeout, final int soTimeout,
	      final String password, final int database, final String clientName) {
	    super(masterName, sentinels, poolConfig, connectionTimeout, soTimeout,
	    		password, database, clientName);
	  }
	  
	  protected class MasterReadWriteListerner extends MasterListener{
		  
		  @Override
		public void run() {
			super.run();
			
		}
		  
	  }

}
