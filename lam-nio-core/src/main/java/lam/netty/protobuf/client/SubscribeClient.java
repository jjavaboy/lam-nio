package lam.netty.protobuf.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import lam.netty.protobuf.model.SubscribeRespProto;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2016年10月4日
* @versio 1.0
*/
public class SubscribeClient {
	
	public void connect(String host, int port) throws Exception{
		EventLoopGroup group = new NioEventLoopGroup();
		
		try{
			Bootstrap b = new Bootstrap();
			b.group(group)
			.channel(NioSocketChannel.class)
			.option(ChannelOption.SO_KEEPALIVE, true)
			.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline p = ch.pipeline();
					p.addLast(new ProtobufVarint32FrameDecoder());
					p.addLast(new ProtobufDecoder(
							SubscribeRespProto.SubscribeResp.getDefaultInstance()));
					p.addLast(new ProtobufVarint32LengthFieldPrepender());
					p.addLast(new ProtobufEncoder());
					p.addLast(new SubscribeClientHandler());
				}
			});
			
			ChannelFuture f = b.connect(host, port).sync();
			
			f.channel().closeFuture().sync();
		}finally{
			group.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws Exception{
		new SubscribeClient().connect("127.0.0.1", 8080);
	}

}
