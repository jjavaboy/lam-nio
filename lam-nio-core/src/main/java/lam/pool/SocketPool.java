package lam.pool;

import java.net.Socket;

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
public class SocketPool extends SuperPool<Socket>{
	
	public static void main(String[] args){
		JedisPool jedisPool = new JedisPool(Protocol.DEFAULT_HOST, Protocol.DEFAULT_PORT);
		Jedis jedis = new Jedis(Protocol.DEFAULT_HOST, Protocol.DEFAULT_PORT);
	}

}
