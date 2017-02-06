package org.lam.redis.test;

import java.io.IOException;

import org.lam.redis.client.ClusterRedisClient;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年2月6日
* @version 1.0
*/
public class ClusterApp {
	
	public static void main(String[] args) {
		String[] nodes = {
				"192.168.204.127:7000",
				"192.168.204.127:7001",
				"192.168.204.127:7002",
				"192.168.204.127:7003",
				"192.168.204.127:7004",
				"192.168.204.127:7005",
				"192.168.204.127:7006"
		};
		ClusterRedisClient client = new ClusterRedisClient(nodes);
		String value = client.get("mykey");
		System.out.println(value);
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
