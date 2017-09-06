import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Date;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import lam.design.pattern.builder.LamGo;
import lam.log.Console;
import lam.pool.SSocket;
import lam.pool.SSocketFactory;
import lam.pool.SSocketPool;
import lam.pool.support.SObjectPoolConfig;
import lam.util.FinalizeUtils;
import redis.clients.jedis.JedisPool;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年3月22日
* @version 1.0
*/
public class SSocketPoolTest {
	
	public static void main(String[] args) {
		GenericObjectPoolConfig config = new GenericObjectPoolConfig();
		config.setMaxTotal(1);
		config.setMaxIdle(0);
		config.setBlockWhenExhausted(true);//default value:true,
		config.setMaxWaitMillis(30000);//this attribute will be worked only when blockWhenExhausted value is true;
									   //(millisencod)default value:-1, 
									   //value -1, it means wait until exists an idle Object in the pool,
		config.setTestOnCreate(Boolean.TRUE.booleanValue());//default value:false
		config.setTestOnBorrow(Boolean.TRUE.booleanValue());//default value:false
		config.setTestOnReturn(Boolean.TRUE.booleanValue());//default value:false
		config.setLifo(Boolean.TRUE.booleanValue());//last in first out,default value:true
		
		SObjectPoolConfig sconfig = new SObjectPoolConfig();
		SSocketFactory ssocketFactory = new SSocketFactory("192.168.204.127", 6378);
		SSocketPool ssocketPool = new SSocketPool(sconfig, ssocketFactory);
		
		long start = System.currentTimeMillis();
		
		for(int i = 0; i < 500; i++){
			SSocket ssocket = null;
			try{
				ssocket = ssocketPool.getResource();
				//ssocket = new SSocket("192.168.204.127", 6378);
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ssocket.getOutputStream()));
				writer.write("get key" + 0 + "\r\n");
				writer.flush();
				BufferedReader reader = new BufferedReader(new InputStreamReader(ssocket.getInputStream()));
				int buf = -1;
				char[] cbuf = new char[2048];
				while((buf = reader.read(cbuf, 0, cbuf.length)) != -1){
					String s = new String(cbuf, 0, buf);
					System.out.print(s);
					if(s.endsWith("\r\n")){
						break ;
					}
				}
				FinalizeUtils.closeQuietly(reader);
				FinalizeUtils.closeQuietly(writer);
				ssocketPool.returnResource(ssocket);
			}catch(Exception e){
				e.printStackTrace();
				ssocketPool.returnBrokenResource(ssocket);
			}
		}
		
		Console.println("cost(ms):" + (System.currentTimeMillis() - start));
		ssocketPool.close();
		
		LamGo lamGo = new LamGo.Builder()
				.setId(1)
				.setNickname("a")
				.setUsername("b")
				.setAddress("c")
				.setCreateDate(new Date())
				.get();
	}

}
