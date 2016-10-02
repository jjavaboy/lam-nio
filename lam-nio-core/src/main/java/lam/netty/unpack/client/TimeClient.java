package lam.netty.unpack.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2016年10月2日
* @versio 1.0
*/
public class TimeClient {
	
	public void connect(String host, int port) throws Exception{
		EventLoopGroup group = new NioEventLoopGroup();
		try{
			Bootstrap b = new Bootstrap();
			b.group(group)
			.channel(NioSocketChannel.class)
			.option(ChannelOption.SO_KEEPALIVE, true)
			.handler(new TimeClientChannelHandler());
			
			ChannelFuture f = b.connect(host, port).sync();
			
			f.channel().closeFuture().sync();
		}finally{
			group.shutdownGracefully();
		}
	}
	
	private class TimeClientChannelHandler extends ChannelInitializer<SocketChannel>{
		@Override
		protected void initChannel(SocketChannel socketChannel) throws Exception {
			socketChannel.pipeline().addLast(new TimeClientHandler());
		}
	}
	
	public static void main(String[] args) throws Exception{
		new TimeClient().connect("127.0.0.1", 8080);
	}

}
