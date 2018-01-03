package org.lam.redis.test;

import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2018年1月2日
* @version 1.0
*/
public class ScanTest {
	
	public static void main(String[] args) {
		Jedis jedis = new Jedis("192.168.204.79", 6378);
		/*Pipeline pipeline = jedis.pipelined();
		for (int i = 7; i < 100; i++) {
			pipeline.set("key" + i, "value" + i);
		}
		List<Object> list = pipeline.syncAndReturnAll();
		for (Object obj : list) {
			System.out.println(obj);
		}*/
		//jedis.scan(0); //scan(int cursor) has been deprecated, use scan(String cursor) instead.
		String endCursor = "0";
		String cursor = endCursor;
		ScanParams scanParams = new ScanParams();
		do {
			ScanResult<String> scanResult = jedis.scan(cursor, scanParams.count(10));
			cursor = scanResult.getStringCursor();
			List<String> list = scanResult.getResult();
			System.out.println("scan cursor:" + cursor);
			for (String s : list) {
				System.out.println(s);
			}
		} while (!endCursor.equals(cursor));
		for (int j = 0; j < 100; j++) {
			jedis.hset("hashkey", "field" + j, "value" + j);
		}
		/* 注意：
		 * 在迭代一个编码为整数集合（intset，一个只由整数值构成的小集合）、 或者编码为压缩列表（ziplist，由不同值构成的一个小哈希或者一个小有序集合）时，
		 * 增量式迭代命令通常会无视 COUNT 选项指定的值， 在第一次迭代就将数据集包含的所有元素都返回给用户。
		 */
		do {
			ScanResult<Map.Entry<String, String>> sResult = jedis.hscan("hashkey", cursor, scanParams.match("field1*").count(5));
			cursor = sResult.getStringCursor();
			System.out.println("hscan cursor:" + cursor);
			for (Map.Entry<String, String> entry : sResult.getResult()) {
				System.out.println(entry.getKey() + "," + entry.getValue());
			}
		} while (!endCursor.equals(cursor));
		
		jedis.close();
	}

}
