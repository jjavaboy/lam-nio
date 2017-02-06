package org.lam.redis.client;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

/**
* <p>
* 
* </p>
* @author linanmiao
* @date 2017年2月6日
* @version 1.0
*/
public class ClusterRedisClient extends JedisCluster{

	//private GenericObjectPoolConfig poolConfig;
	
	public ClusterRedisClient(String node){
		this(new String[]{node});
	}
	
	public ClusterRedisClient(String[] nodes){
		super(toNodes(nodes), defaultGenericObjectPoolConfig());
	}
	
	public ClusterRedisClient(String[] nodes, int timeout){
		super(toNodes(nodes), timeout, defaultGenericObjectPoolConfig());
	}
	
	public ClusterRedisClient(String[] nodes, int timeout, final GenericObjectPoolConfig poolConfig){
		super(toNodes(nodes), timeout, poolConfig);
	}
	
	public ClusterRedisClient(Set<HostAndPort> jedisClusterNodes, int timeout, final GenericObjectPoolConfig poolConfig){
		super(jedisClusterNodes, timeout, poolConfig);
	}
	
	private static GenericObjectPoolConfig defaultGenericObjectPoolConfig(){
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		poolConfig.setMaxTotal(GenericObjectPoolConfig.DEFAULT_MAX_TOTAL);
		poolConfig.setMinIdle(GenericObjectPoolConfig.DEFAULT_MIN_IDLE);
		poolConfig.setMaxIdle(GenericObjectPoolConfig.DEFAULT_MAX_IDLE);
		//..other attribute to be configurated
		//this.poolConfig = poolConfig;
		return poolConfig;
	}
	
	private static Set<HostAndPort> toNodes(String[] nodes){
		Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>(nodes.length);
		String[] hap;
		for(String node : nodes){
			hap = node.split(":");
			jedisClusterNodes.add(new HostAndPort(hap[0], Integer.parseInt(hap[1])));
		}
		return jedisClusterNodes;
	}
	
}
