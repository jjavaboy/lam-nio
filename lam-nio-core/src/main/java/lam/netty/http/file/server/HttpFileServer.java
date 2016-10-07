package lam.netty.http.file.server;

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
import io.netty.handler.stream.ChunkedWriteHandler;

/**
* <p>
* http file server
* </p>
* @author linanmiao
* @date 2016年10月7日
* @versio 1.0
*/
public class HttpFileServer {
	
	private static Logger logger = LoggerFactory.getLogger(HttpFileServer.class);
	
	private static final String DEFAULT_URL = "";
	
	public void run(final int port, final String url) throws Exception{
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
					p.addLast("http-encoder", new HttpResponseEncoder());
					p.addLast("http-chunked", new ChunkedWriteHandler());
					p.addLast("fileServerHandler", new HttpFileServerHandler(url));
				}
			});
			
			ChannelFuture f = b.bind("127.0.0.1", port).sync();
			
			logger.info("lister at {}:{}", "127.0.0.1", port);
			
			f.channel().closeFuture().sync();
		}finally{
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws Exception{
		new HttpFileServer().run(8080, DEFAULT_URL);
	}

}
