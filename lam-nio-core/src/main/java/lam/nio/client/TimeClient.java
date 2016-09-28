package lam.nio.client;
/**
* <p>
* time client
* </p>
* @author linanmiao
* @date 2016年9月28日
* @versio 1.0
*/
public class TimeClient {
	
	public static void main(String[] args){
		String host = "127.0.0.1";
		int port = 8080;
		if(args != null && args.length > 0){
			host = args[0];
			try{				
				port = Integer.parseInt(args[1]);
			}catch(NumberFormatException e){
				e.printStackTrace();
			}
		}
		
		new Thread(new TimeClientHandler(host, port), "TimeClientHandler-Thread").start();
	}

}
