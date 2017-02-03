package org.lam.redis.client;

import com.google.gson.Gson;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisMonitor;

/**
* <p>
* redis client which send a command:monitor
* </p>
* @author linanmiao
* @date 2017年2月3日
* @version 1.0
*/
public class RedisMonitorClient {
	
	private Jedis jedis;
	
	public RedisMonitorClient(Jedis jedis){
		this.jedis = jedis;
	}
	
	public void start(){
		jedis.monitor(new JedisMonitor(){
			
			private Gson gson = new Gson();

			@Override
			public void onCommand(String command) {
				
				MonitorCommand c = new MonitorCommand(command);
				System.out.println(gson.toJson(c));
				
			}
		});
	}
	
	public void destroy(){
		jedis.close();
	}
		
	private static class MonitorCommand {
		
		private static final char SPLIT = ' ';
		
		private String timestamp;
		private int database;
		private String host;
		private int clinetPort;
		private String command;
		
		public MonitorCommand(String line){
			parse(line);
		}
		
		/**
		 * argument line like:1486104759.324749 [0 192.168.204.107:57161] "SADD" "setkey" "key1"
		 */
		private void parse(String line){
			int firstIdx = line.indexOf(SPLIT);
			this.timestamp = line.substring(0, firstIdx);
			int dHostPortIdx = line.indexOf(']');
			String dHostPort = line.substring(firstIdx + 1, dHostPortIdx + 1);
			dHostPort = dHostPort.replace("[", "");
			dHostPort = dHostPort.replace("]", "");
			
			int dHostPortMidd = dHostPort.indexOf(SPLIT);
			this.database = Integer.parseInt(dHostPort.substring(0, dHostPortMidd));
			
			String[] ss = dHostPort.substring(dHostPortMidd + 1).split(":");
			this.host = ss[0];
			this.clinetPort = Integer.parseInt(ss[1]);
			
			this.command = line.substring(dHostPortIdx + 1);
		}
		
		public String getTimestamp() {
			return timestamp;
		}
		
		public int getDatabase() {
			return database;
		}
		
		public String getHost() {
			return host;
		}
		
		public int getClinetPort() {
			return clinetPort;
		}
		
		public String getCommand() {
			return command;
		}
	}
	
	public static void main(String[] args){
		Jedis jedis = new Jedis("192.168.204.127", 6378);
		RedisMonitorClient monitorClient = new RedisMonitorClient(jedis);
		try{
			monitorClient.start();
		}finally{
			monitorClient.destroy();
		}
		/*String line = "1486104759.324749 [0 192.168.204.107:57161] \"SADD\" \"setkey\" \"key1\"";
		MonitorCommand c = new MonitorCommand(line);
		System.out.println(new Gson().toJson(c));*/
	}

}
