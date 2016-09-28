package lam.nio.server;
/**
* <p>
* time server
* </p>
* @author linanmiao
* @date 2016年9月28日
* @versio 1.0
*/
public class TimeServer {
	
	public static void main(String[] args){
		int port = 8080;
		if(args != null && args.length > 0){
			try{
				port = Integer.parseInt(args[0]);
			}catch(NumberFormatException e){
				e.printStackTrace();
			}
		}
		new Thread(new MultiplexerTimeServer(port), "MultiplexerTimeServer-Thread-" + port).start();
	}

}
