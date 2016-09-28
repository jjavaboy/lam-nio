package lam.nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import lam.util.DateUtil;

/**
 * <p>
 * server class
 * </p>
 * @author linam@joysim.cn
 * @date 2016年9月28日
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
			
			System.out.println(String.format("%s %s-listen at port:%d", DateUtil.getCurrentTime(), getClass().getName() ,port));
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
					iter.remove();
					try{
						handleInput(key);
					}catch(Exception e){
						e.printStackTrace();
						if(key != null){
							key.cancel();
							if(key.channel() != null){
								key.channel().close();
							}
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//多路复用器关闭后，注册在selctor上的channel和pipe都自动去注册并关闭，所以不需要重复关闭资源
		if(selector != null){
			try {
				selector.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void handleInput(SelectionKey key) throws IOException{
		if(key.isValid()){
			//accept the new connection
			if(key.isAcceptable()){
				ServerSocketChannel ssChannel = (ServerSocketChannel) key.channel();
				SocketChannel sChannel = ssChannel.accept();
				sChannel.configureBlocking(false);
				//add new connection to the selector
				sChannel.register(selector, SelectionKey.OP_READ);
			}
			if(key.isReadable()){
				//read the data
				SocketChannel sChannel = (SocketChannel) key.channel();
				//allocate 1KB to buffer
				ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
				int readBytes = sChannel.read(byteBuffer);
				if(readBytes > 0){
					byteBuffer.flip();
					byte[] bytes = new byte[byteBuffer.remaining()];
					byteBuffer.get(bytes);
					String body = new String(bytes, "utf-8");
					
					System.out.println(String.format("%s %s-receive:%s", DateUtil.getCurrentTime(), getClass().getName(), body));
					
					String currentTime = null;
					if("QEURY TIME".equalsIgnoreCase(body)){
						currentTime = new Date().toString();
					}else{
						currentTime = "BAD PARAMETER";
					}
					doWrite(sChannel, currentTime);
				}else if(readBytes < 0){
					//对端链路关闭
					key.cancel();
					sChannel.close();
				}else{
					//读取0字节，忽略
				}
			}
		}
	}

	/**
	 * 结果写回客户端
	 */
	private void doWrite(SocketChannel sChannel, String response) throws IOException {
		if(response != null && response.trim().length() > 0){
			byte[] bytes = response.getBytes();
			ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
			byteBuffer.put(bytes);
			byteBuffer.flip();
			sChannel.write(byteBuffer);
		}
		
	}

}
