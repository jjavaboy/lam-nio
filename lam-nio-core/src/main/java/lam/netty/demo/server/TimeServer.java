package lam.netty.demo.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * <p>
 * time server
 * </p>
 * 
 * @author linanmiao
 * @date 2016年9月30日
 * @versio 1.0
 */
public class TimeServer {
	
	private static Logger logger = LoggerFactory.getLogger(TimeServer.class);

	public void bind(int port) throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap
				.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 1024)
				.childOption(ChannelOption.SO_KEEPALIVE, true)
				.childHandler(new TimeChildChannelHandler());

			// bind port
			ChannelFuture channelFuture = serverBootstrap.bind(port).sync();

			logger.info("listen at port:" + port);

			// wait for close
			channelFuture.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	private class TimeChildChannelHandler extends ChannelInitializer<SocketChannel> {
		@Override
		protected void initChannel(SocketChannel socketChannel) throws Exception {
			socketChannel.pipeline().addLast(new TimeServerHandler());
		}
	}
	
	public static void main(String[] args) throws Exception{
		int port = 8080;
		if(args != null && args.length > 0){
			try{
				port = Integer.parseInt(args[0]);
			}catch(NumberFormatException e){
				logger.error("main error==>>", e);
			}
		}
		new TimeServer().bind(port);
	}

}
