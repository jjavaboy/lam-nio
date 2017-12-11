package lam.netty.demo.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lam.log.Console;

/**
* <p>
* time client channel handler
* </p>
* @author linanmiao
* @date 2016年10月1日
* @versio 1.0
*/
public class TimeClientHandler extends ChannelInboundHandlerAdapter{
	
	private static Logger logger = LoggerFactory.getLogger(TimeClientHandler.class);
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		String request = "QUERY TIME";
		byte[] bytes = request.getBytes();
		//allocate a ByteBuf with length of request string
		ByteBuf byteBuf = Unpooled.buffer(bytes.length);
		//write data into ByteBuf
		byteBuf.writeBytes(bytes);
		ctx.writeAndFlush(byteBuf);
		
		//logger.info("channelActive, send==>>" + request);
		Console.println("channelActive, send==>>" + request);
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf byteBuf = (ByteBuf) msg;
		byte[] bytes = new byte[byteBuf.readableBytes()];
		byteBuf.readBytes(bytes);
		String response = new String(bytes, "utf-8");
		//logger.info("channelRead, receive==>>" + response);
		Console.println("channelRead, receive==>>" + response);
		
		//the example just for test
		//this ChannelHandler uses only once, close it when get the response.
		ctx.close();
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable t) throws Exception {
		//logger.error("exceptionCaught, error==>>", t);
		Console.println("exceptionCaught, error==>>%s", t.getMessage());
		ctx.close();
	}

}
