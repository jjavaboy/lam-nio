package lam.nio.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import lam.util.DateUtil;

/**
* <p>
* time client handler
* </p>
* @author linanmiao
* @date 2016年9月28日
* @versio 1.0
*/
public class TimeClientHandler implements Runnable{
	
	private String host;
	
	private int port;
	
	private volatile boolean stop;
	
	private Selector selector;
	
	private SocketChannel socketChannel;
	
	public TimeClientHandler(String host, int port){
		this.host = host;
		this.port = port;
		try {
			selector = Selector.open();
			socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(false);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public void run() {
		try {
			doConnect();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		while(!stop){
			try {
				selector.select(1000); //select after 1 second
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
						key.cancel();
						if(key.channel() != null){
							key.channel().close();
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			} 
		}
		
		//多路利用器关闭后，注册在selector上面的channel和pile被自动去注册，无需重复释放资源
		if(selector != null){
			try {
				selector.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void handleInput(SelectionKey key) throws IOException{
		if(key.isValid()){
			SocketChannel sChannel = (SocketChannel) key.channel();
			if(key.isConnectable()){
				if(sChannel.finishConnect()){
					sChannel.register(selector, SelectionKey.OP_READ);
					doWrite(sChannel);
				}else{
					//连接失败，退出
					System.exit(1);
				}
			}
			if(key.isReadable()){
				ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
				int readBytes = sChannel.read(byteBuffer);
				if(readBytes > 0){
					byteBuffer.flip();
					byte[] bytes = new byte[byteBuffer.remaining()];
					byteBuffer.get(bytes);
					String response = new  String(bytes, "utf-8");
					
					System.out.println(String.format("%s %s-response:%s", DateUtil.getCurrentTime(), getClass().getName(), response));
					
					key.cancel();
					sChannel.close();
					
					this.stop = true;
				}else if(readBytes < 0){
					//对端链路关闭
					key.cancel();
					sChannel.close();
				}else{
					//读到0字节，忽略
				}
			}
		}
	}
	
	private void doConnect() throws IOException{
		boolean connected = socketChannel.connect(new InetSocketAddress(host, port));
		if(connected){
			socketChannel.register(selector, SelectionKey.OP_READ);
			doWrite(socketChannel);
		}else{
			socketChannel.register(selector, SelectionKey.OP_CONNECT);
		}
	}
	
	private void doWrite(SocketChannel socketChannel) throws IOException{
		byte[] bytes = "QEURY TIME".getBytes();
		ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
		byteBuffer.put(bytes);
		byteBuffer.flip();
		socketChannel.write(byteBuffer);
		if(!byteBuffer.hasRemaining()){
			System.out.println(String.format("%s %s-Send parameter to server success.", DateUtil.getCurrentTime(), getClass().getName()));
		}
	}

}
