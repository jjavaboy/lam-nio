package lam.netty.protobuf.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import lam.netty.protobuf.model.SubscribeReqProto;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2016年10月4日
* @versio 1.0
*/
public class SubscribeServer {
	
	private static Logger logger = LoggerFactory.getLogger(SubscribeServer.class);
	
	public void bind(int port) throws Exception{
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try{
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 1024)
			.childOption(ChannelOption.SO_KEEPALIVE, true)
			.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline p = ch.pipeline();
					p.addLast(new ProtobufVarint32FrameDecoder());
					p.addLast(new ProtobufDecoder(
							SubscribeReqProto.SubscribeReq.getDefaultInstance()));
					p.addLast(new ProtobufVarint32LengthFieldPrepender());
					p.addLast(new ProtobufEncoder());
					p.addLast(new SubscribeServerHandler());
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
		new SubscribeServer().bind(8080);
	}

}
