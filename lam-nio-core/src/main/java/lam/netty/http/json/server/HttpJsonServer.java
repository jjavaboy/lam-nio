package lam.netty.http.json.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import lam.netty.http.json.decoder.HttpJsonRequestDecoder;
import lam.netty.http.json.encoder.HttpJsonResponseEncoder;
import lam.netty.http.json.model.Order;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2016年10月10日
* @versio 1.0
*/
public class HttpJsonServer {
	
	private static Logger logger = LoggerFactory.getLogger(HttpJsonServer.class);
	
	public void run(int port) throws Exception{
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try{
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline p = ch.pipeline();
					p.addLast("http-decoder", new HttpRequestDecoder());
					p.addLast("http-aggregator", new HttpObjectAggregator(65535));
					p.addLast("json-decoder", new HttpJsonRequestDecoder(Order.class));
					p.addLast("http-encoder", new HttpResponseEncoder());
					p.addLast("json-encoder", new HttpJsonResponseEncoder());
					p.addLast("jsonServerHandler", new HttpJsonServerHandler());
				}
			});
			
			ChannelFuture f = b.bind(port).sync();
			
			logger.info("listen at port:" + port);
			
			f.channel().closeFuture().sync();
		}finally{
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws Exception{
		new HttpJsonServer().run(8080);
	}

}
