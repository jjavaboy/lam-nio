package lam.nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * <p>
 * server class
 * </p>
 * @author linam@joysim.cn
 * @date 2016Äê9ÔÂ28ÈÕ
 * @version 1.0
 */
public class MultiplexerTimeServer implements Runnable{

	private Selector selector;
	
	private ServerSocketChannel serverSocketChannel;
	
	private boolean stop;
	
	public MultiplexerTimeServer(int port) {
		try {
			selector = Selector.open();
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.socket().bind(new InetSocketAddress(port), 1024);
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println(getClass() + " listen at port:" + port);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void stop(){
		this.stop = true;
	}
	
	@Override
	public void run() {
		while(!stop){
			try {
				selector.select(1000); //select after 1 secod 
				Set<SelectionKey> set = selector.selectedKeys();
				Iterator<SelectionKey> iter = set.iterator();
				SelectionKey key = null;
				while(iter.hasNext()){
					key = iter.next();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}//
		}
	}
	
	public void handleInput(SelectionKey key) throws IOException{
		if(key.isValid()){
			//accept the new connection
			if(key.isAcceptable()){
				ServerSocketChannel ssChannel = (ServerSocketChannel) key.channel();
				ssChannel.configureBlocking(false);
				ssChannel.register(selector, SelectionKey.OP_READ);
			}
		}
	}

}
