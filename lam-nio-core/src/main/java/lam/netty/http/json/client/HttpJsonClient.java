package lam.netty.http.json.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import lam.netty.http.json.decoder.HttpJsonResponseDecoder;
import lam.netty.http.json.encoder.HttpJsonRequestEncoder;
import lam.netty.http.json.model.Order;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2016年10月10日
* @versio 1.0
*/
public class HttpJsonClient {
	
	public void connect(String host, int port) throws Exception{
		EventLoopGroup group = new NioEventLoopGroup();
		try{
			Bootstrap b = new Bootstrap();
			b.group(group)
			.channel(NioSocketChannel.class)
			.option(ChannelOption.TCP_NODELAY, true)
			.handler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline p = ch.pipeline();
					p.addLast("http-decoder", new HttpResponseDecoder());
					p.addLast("http-aggregator", new HttpObjectAggregator(65535));
					p.addLast("json-decoder", new HttpJsonResponseDecoder(Order.class));
					p.addLast("http-encoder", new HttpRequestEncoder());
					p.addLast("json-encoder", new HttpJsonRequestEncoder());
					p.addLast("jsonClientHandler", new HttpJsonClientHandler());
				}
			});
			
			ChannelFuture f = b.connect(host, port).sync();
			
			f.channel().closeFuture().sync();
		}finally{
			group.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws Exception{
		new HttpJsonClient().connect("127.0.0.1", 8080);
	}

}
