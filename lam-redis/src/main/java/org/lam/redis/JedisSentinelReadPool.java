package org.lam.redis;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.lam.redis.model.SNode;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.Pool;

/**
* <p>
* It should be only read pool, not read and write pool.
* This class should extends Pool<T>, not JedisSentinelPool.
* </p>
* @author linanmiao
* @date 2017年1月10日
* @versio 1.0
*/
public class JedisSentinelReadPool extends JedisSentinelPool{
	
	//private ReadPool<Jedis> readPool;

	//private volatile JedisSentinelPool masterSentinelPool;
	private volatile HostAndPort currentHostSlave;
	private volatile JedisReadFactory jedisReadFactory;
	private Set<String> sentinels;
	private volatile String masterName;
	protected Set<MasterReadListener> masterReadListeners;
	
	public JedisSentinelReadPool(String masterName, Set<String> sentinels){
		this(masterName, sentinels, new GenericObjectPoolConfig());
	}
	
	public JedisSentinelReadPool(String masterName, Set<String> sentinels,
		      final GenericObjectPoolConfig poolConfig){
		this(masterName, sentinels, poolConfig, Protocol.DEFAULT_TIMEOUT, null, Protocol.DEFAULT_DATABASE, null);
	}
	
	public JedisSentinelReadPool(String masterName, Set<String> sentinels, String password){
		this(masterName, sentinels, new GenericObjectPoolConfig(), Protocol.DEFAULT_TIMEOUT, password,
				Protocol.DEFAULT_DATABASE, null);
	}
	
	public JedisSentinelReadPool(String masterName, Set<String> sentinels,
      final GenericObjectPoolConfig poolConfig, int timeout, final String password,
      final int database, final String clientName){
		this(masterName, sentinels, poolConfig, timeout, timeout, password, database, clientName);
	}
	
	public JedisSentinelReadPool(String masterName, Set<String> sentinels,
      final GenericObjectPoolConfig poolConfig, final int timeout, final int soTimeout,
      final String password, final int database){
		this(masterName, sentinels, poolConfig, timeout, soTimeout, password, database, null);
	}
	
	public JedisSentinelReadPool(String masterName, Set<String> sentinels,
			final GenericObjectPoolConfig poolConfig, final int connectionTimeout, final int soTimeout,
			final String password, final int database, final String clientName){
		super(masterName, sentinels, poolConfig, connectionTimeout, soTimeout, password, database, clientName);
		this.poolConfig = poolConfig;
	    this.connectionTimeout = connectionTimeout;
	    this.soTimeout = soTimeout;
	    this.password = password;
	    this.database = database;
	    this.clientName = clientName;
		
		this.sentinels = sentinels;
		this.masterName = masterName;
		this.masterReadListeners = new HashSet<MasterReadListener>();
		
		HostAndPort currentHostMaster = getCurrentHostMaster();
		currentHostSlave = initCurrentSlave(sentinels, currentHostMaster);
		initPool(currentHostSlave);
	}

	private void initPool(HostAndPort currentHostSlave) {
		if(jedisReadFactory == null){
			jedisReadFactory = new JedisReadFactory(currentHostSlave.getHost(), currentHostSlave.getPort(),
					connectionTimeout, soTimeout, password, database, clientName, false, null, null, null);
			initPool(poolConfig, jedisReadFactory);
		}else{
			jedisReadFactory.setHostAndPort(currentHostSlave);
			internalPool.clear();
		}
		log.info("Created JedisReadPool to Slave at " + currentHostSlave);
	}
	
	private HostAndPort initCurrentSlave(Set<String> sentinels, HostAndPort currentHostMaster){
		//get one slave host according to master host
		Jedis master = null;
		HostAndPort slave = null;
		try{
			master = new Jedis(currentHostMaster.getHost(), currentHostMaster.getPort());
			String reply = master.info("replication");
			String[] replys = reply.split("\n");
			
			for(String line : replys){
				line = line.trim().replace("\r", "");
				if(line.indexOf('#') == 0 || line.indexOf(':') == -1){
					continue ;
				}
				//
				//line may be like this:
				//slave0:ip=192.168.204.127,port=6379,state=online,offset=9252687,lag=1
				//
				SNode slaveNode = SNode.parse(line, ':');
				if(!slaveNode.getKey().startsWith("slave")){
					continue ;
				}
				Set<SNode> set = SNode.parseToSet(slaveNode.getValue(), ",");
				slave = toHostAndPort(set);
				if(slave != null){
					break ;
				}
			}
		}catch(JedisException e){
			log.warning("error:" + e.getMessage());
		}finally{
			if(master != null){
				master.close();
			}
		}
		if(slave == null){
			throw new RuntimeException("Can not get slave address");
		}
		for(String sentinel : sentinels){
			final HostAndPort sentinelHap = HostAndPort.parseString(sentinel);
			MasterReadListener listener = new MasterReadListener(masterName, sentinelHap.getHost(), sentinelHap.getPort());
			listener.setDaemon(true);
			masterReadListeners.add(listener);
			listener.start();
		}
		return slave;
	}
	
	private HostAndPort toHostAndPort(Set<SNode> set){
		String slaveHost = null;
		int slavePort = -1;
		for(SNode snode : set){
			if("ip".equals(snode.getKey())){
				slaveHost = snode.getValue();
			}else if("port".equals(snode.getKey())){
				slavePort = Integer.parseInt(snode.getValue());
			}
		}
		if(slaveHost != null && slavePort != -1){
			return new HostAndPort(slaveHost, slavePort);
		}
		return null;
	}
	
	public Jedis getResource(){
		while(true){
			Jedis jedis = super.getResource();
			jedis.setDataSource(this);
			
			final HostAndPort slave = currentHostSlave;
			final HostAndPort slaveConnection = new HostAndPort(jedis.getClient().getHost(), 
					jedis.getClient().getPort());
			if(slave.equals(slaveConnection)){
				return jedis;
			}else{
				super.returnBrokenResource(jedis);
			}
		}
	}
	
	protected class MasterReadListener extends Thread{
		protected String masterName;
		protected String host;
		protected int port;
		protected long subscribeRetryWaitTimeMillis;
		protected AtomicBoolean running = new AtomicBoolean(false);
		protected volatile Jedis jedis;
		
		protected MasterReadListener(){}
		
		protected MasterReadListener(String masterName, String host, int port){
			super(String.format("MasterReadListener-%s[%s:%d]", masterName, host, port));
			this.masterName = masterName;
			this.host = host;
			this.port = port;
			this.subscribeRetryWaitTimeMillis = 5000;
		}
		
		protected MasterReadListener(String masterName, String host, int port, long subscribeRetryWaitTimeMillis){
			this(masterName, host, port);
			this.subscribeRetryWaitTimeMillis = subscribeRetryWaitTimeMillis;
		}
		
		@Override
		public void run() {
			running.set(true);
			while(running.get()){
				try{
					jedis = new Jedis(host, port);
					if(!running.get()){
						break;
					}
					//subscribe +switch-master even
					jedis.subscribe(new JedisPubSub(){
						@Override
						public void onMessage(String channel, String message) {
							log.info(String.format("Sentinel[%s:%d] publish message:%s", host, port, message));
							String[] messageArray = message.split(" ");
							if(messageArray.length > 3){
								if(masterName.equals(messageArray[0])){
									int port = Integer.parseInt(messageArray[4]);
									HostAndPort newMaster = new HostAndPort(messageArray[3], port);
									currentHostSlave = initCurrentSlave(sentinels, newMaster);
									initPool(currentHostSlave);
								}else{
									//Invalid masterName
									log.warning(String.format("master from publish message:%s, but our masterName:%s", messageArray[4], masterName));
								}
							}else{
								//Invalid message
								log.warning("length of message published from Sentinel is invalid.");
							}
						}
					}, "+switch-master");
				}catch(JedisConnectionException e){
					if(running.get()){
						log.warning(String.format("sleep %d and retry to connection redis", subscribeRetryWaitTimeMillis));
						try {
							Thread.sleep(subscribeRetryWaitTimeMillis);
						} catch (InterruptedException e1) {
							log.warning(e1.getMessage());
						}
					}
				}finally{
					if(jedis != null){
						jedis.close();
					}
				}
			}
		}
		
		/**
		 * listener shutdown 
		 */
		public void shutdown(){
			log.info(String.format("%s shutdown", getName()));
			running.set(false);
			if(jedis != null){
				try{
				jedis.disconnect();
				}catch(JedisException e){
					log.warning("listener shotdown error:" + e.getMessage());
				}
			}
		}
	}
	
	/*protected class ReadPool<T> extends Pool<T>{
		public ReadPool(){}
		
		public ReadPool(final GenericObjectPoolConfig poolConfig, PooledObjectFactory<T> factory){
			initPool(poolConfig, factory);
		}
		
		protected GenericObjectPool<T> getInternalPool(){
			return internalPool;
		}
	}*/
}
