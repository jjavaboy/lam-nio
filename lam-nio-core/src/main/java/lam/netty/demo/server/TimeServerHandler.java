package lam.netty.demo.server;

import java.net.InetSocketAddress;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
* <p>
* 
* io.netty.channel.ChannelInboundHandlerAdapter extends io.netty.channel.ChannelHandler<br/>
* TimeServerHandler extends io.netty.channel.ChannelHandlerAdapter<br/>
* So:<br/>
* TimeServerHandler is an instance of io.netty.channel.ChannelHandler
* </p>
* @author linanmiao
* @date 2016年9月30日
* @versio 1.0
*/
public class TimeServerHandler extends ChannelInboundHandlerAdapter{
	
	private static Logger logger = LoggerFactory.getLogger(TimeServerHandler.class);
	
	//this event will be called when netty client connect to server
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		InetSocketAddress inetSocketAddress =  (InetSocketAddress) ctx.channel().remoteAddress();
		logger.info("channelActive, {}:{}", inetSocketAddress.getHostName(), inetSocketAddress.getPort());
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf byteBuf = (ByteBuf) msg;
		byte[] bytes = new byte[byteBuf.readableBytes()];
		byteBuf.readBytes(bytes);
		String request = new String(bytes, "utf-8");
		String response = null;
		if("QUERY TIME".equals(request)){
			response = new Date().toString();
		}else{
			response = "BAD PARAMETER";
		}
		ByteBuf responseByteBuf = Unpooled.copiedBuffer(response.getBytes());
		ctx.writeAndFlush(responseByteBuf);

		logger.info("channelRead, receive==>>{}, send==>>{}", request, response);
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	
	//this event will be called when netty client close
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		InetSocketAddress inetSocketAddress =  (InetSocketAddress) ctx.channel().remoteAddress();
		logger.info("channelInactive, {}:{}", inetSocketAddress.getHostName(), inetSocketAddress.getPort());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("exceptionCaught error==>>", cause);
		ctx.close();
	}
	
}
