package org.lam.redis;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.lam.redis.model.SNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.Pool;

/**
* <p>
* It should be only read pool, not read and write pool, it means you get slave redis.
* This class should extends Pool<T>, not JedisSentinelPool.
* </p>
* @author linanmiao
* @date 2017年1月10日
* @versio 1.0
*/
public class JedisSentinelReadPool extends Pool<Jedis>{
	
	private static Logger logger = LoggerFactory.getLogger(JedisSentinelReadPool.class);
	
	protected GenericObjectPoolConfig poolConfig;

	protected int connectionTimeout = Protocol.DEFAULT_TIMEOUT;
	protected int soTimeout = Protocol.DEFAULT_TIMEOUT;
	protected int database = Protocol.DEFAULT_DATABASE;
	protected String password;
	protected String clientName;
	
	private Set<String> sentinels;
	private volatile String masterName;
	private volatile HostAndPort master;
	private volatile HostAndPort slave;
	private volatile JedisReadFactory jedisReadFactory;
	protected Set<MasterListener> masterListeners;
	
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
		this.poolConfig = poolConfig;
	    this.connectionTimeout = connectionTimeout;
	    this.soTimeout = soTimeout;
	    this.password = password;
	    this.database = database;
	    this.clientName = clientName;
		
		this.sentinels = sentinels;
		this.masterName = masterName;
		this.masterListeners = new HashSet<MasterListener>();
		
		this.master = initMaster(this.sentinels, this.masterName);
		this.slave = initSlave(this.master);
		initPool(this.slave);
		initMasterListener(this.sentinels);
	}
	
	private HostAndPort initMaster(Set<String> sentinels, String masterName){
		for(String sentinel : sentinels){
			logger.info(String.format("Get master according to the sentinel:%s", sentinel));
			HostAndPort sentinelHost = toHostAndPort(sentinel);
			Jedis sentinelJedis = new Jedis(sentinelHost.getHost(), sentinelHost.getPort());
			try{
				List<String> reply = sentinelJedis.sentinelGetMasterAddrByName(masterName);
				if(reply == null || reply.size() < 2){
					logger.warn("reply of command:sentinel get-master-addr-by-name mymaster is invalid, reply:{}", reply);
					continue ;
				}
				return new HostAndPort(reply.get(0), Integer.parseInt(reply.get(1)));
			}catch(JedisException e){
				logger.error(String.format("Get master fail, sentinel:%s", sentinel), e);
			}finally{
				sentinelJedis.close();
			}
		}
		return null;
	}
	
	private HostAndPort initSlave(HostAndPort master){
		//get one slave host according to master host
		Jedis masterJedis = null;
		HostAndPort slave = null;
		try{
			masterJedis = new Jedis(master.getHost(), master.getPort());
			String reply = masterJedis.info("replication");
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
					return slave;
				}
			}
			throw new RuntimeException("Can not get slave address");
		}catch(JedisException e){
			logger.error("Fail to get slave address, error:" + e.getMessage());
			throw new RuntimeException("Fail to get slave address");
		}finally{
			if(master != null){
				masterJedis.close();
			}
		}
	}
	
	private void initPool(HostAndPort slave) {
		if(jedisReadFactory == null){
			jedisReadFactory = new JedisReadFactory(slave.getHost(), slave.getPort(),
					connectionTimeout, soTimeout, password, database, clientName, false, null, null, null);
			initPool(poolConfig, jedisReadFactory);
		}else{
			jedisReadFactory.setHostAndPort(slave);
			internalPool.clear();
		}
		logger.info("Created JedisReadPool to Slave at " + slave);
	}
	
	private void initMasterListener(Set<String> sentinels){
		for(String sentinel : sentinels){
			final HostAndPort sentinelHap = HostAndPort.parseString(sentinel);
			MasterListener listener = new MasterListener(masterName, sentinelHap.getHost(), sentinelHap.getPort());
			listener.setDaemon(true);
			masterListeners.add(listener);
			listener.start();
		}
	}
	
	private HostAndPort toHostAndPort(String sentinel){
		int idx = -1;
		if((idx = sentinel.indexOf(':')) == -1){
			throw new IllegalArgumentException("sentinel string does not contains char ':'");
		}
		return new HostAndPort(sentinel.substring(0, idx), Integer.parseInt(sentinel.substring(idx + 1)));
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
	
	public void destroy(){
		for(MasterListener listener : masterListeners){
			listener.shutdown();
		}
		super.destroy();
	}
	
	public Jedis getResource(){
		while(true){
			Jedis jedis = super.getResource();
			jedis.setDataSource(this);
			
			final HostAndPort currentSlave = this.slave;
			final HostAndPort slaveConnection = new HostAndPort(jedis.getClient().getHost(), 
					jedis.getClient().getPort());
			if(currentSlave.equals(slaveConnection)){
				return jedis;
			}else{
				//destroy jedis object
				super.returnBrokenResourceObject(jedis);
			}
		}
	}
	
	protected class MasterListener extends Thread{
		protected String masterName;
		protected String host;
		protected int port;
		protected long subscribeRetryWaitTimeMillis;
		protected AtomicBoolean running = new AtomicBoolean(false);
		protected volatile Jedis jedis;
		
		protected MasterListener(){}
		
		protected MasterListener(String masterName, String host, int port){
			super(String.format("MasterReadListener-%s[%s:%d]", masterName, host, port));
			this.masterName = masterName;
			this.host = host;
			this.port = port;
			this.subscribeRetryWaitTimeMillis = 5000;
		}
		
		protected MasterListener(String masterName, String host, int port, long subscribeRetryWaitTimeMillis){
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
							logger.info(String.format("Sentinel[%s:%d] publish message:%s", host, port, message));
							String[] messageArray = message.split(" ");
							if(messageArray.length > 3){
								if(masterName.equals(messageArray[0])){
									int port = Integer.parseInt(messageArray[4]);
									master = new HostAndPort(messageArray[3], port);
									slave = initSlave(master);
									initPool(slave);
								}else{
									//Invalid masterName
									logger.warn(String.format("master from publish message:%s, but our masterName:%s", messageArray[4], masterName));
								}
							}else{
								//Invalid message
								logger.warn("length of message published from Sentinel is invalid.");
							}
						}
					}, "+switch-master");
				}catch(JedisConnectionException e){
					if(running.get()){
						logger.error(String.format("sleep %d and retry to connection redis", subscribeRetryWaitTimeMillis));
						try {
							Thread.sleep(subscribeRetryWaitTimeMillis);
						} catch (InterruptedException e1) {
							logger.error(e1.getMessage());
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
			logger.info(String.format("%s shutdown", getName()));
			running.set(false);
			if(jedis != null){
				try{
				jedis.disconnect();
				}catch(JedisException e){
					logger.error("listener shotdown error:" + e.getMessage());
				}
			}
		}
	}

}
