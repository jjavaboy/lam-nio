package lam.netty.unpacksolve.delimiter.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2016年10月3日
* @versio 1.0
*/
public class EchoDelimiterClient {
	
	public void connect(String host, int port) throws Exception{
		EventLoopGroup group = new NioEventLoopGroup();
		try{
			Bootstrap b = new Bootstrap();
			b.group(group)
			.channel(NioSocketChannel.class)
			.option(ChannelOption.SO_KEEPALIVE, true)
			.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel socketChannel) throws Exception {
					ByteBuf delimiterByteBuf = Unpooled.copiedBuffer("$_".getBytes());
					ChannelPipeline p = socketChannel.pipeline();
					p.addLast(new DelimiterBasedFrameDecoder(1024, delimiterByteBuf));
					p.addLast(new StringDecoder());
					p.addLast(new EchoDelimiterClientHandler("$_"));
				}
			});
			
			ChannelFuture f = b.connect(host, port).sync();
			
			f.channel().closeFuture().sync();
		}finally{
			group.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws Exception{
		new EchoDelimiterClient().connect("127.0.0.1", 8080);
	}

}
