package lam.netty.unpack.server;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2016年10月2日
* @versio 1.0
*/
public class TimeServerHandler extends ChannelInboundHandlerAdapter{
	
	private static Logger logger = LoggerFactory.getLogger(TimeServerHandler.class);
	
	private AtomicInteger counter = new AtomicInteger(0);
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf byteBuf = (ByteBuf) msg;
		byte[] bytes = new byte[byteBuf.readableBytes()];
		byteBuf.readBytes(bytes);
		String body = new String(bytes, "utf-8").substring(0, bytes.length - System.getProperty("line.separator").length());
		String response = null;
		if("QUERY TIME".equals(body)){
			response = new Date().toString();
		}else{
			response = "BAD PARAMETER";
		}
		response += System.getProperty("line.separator");
		ByteBuf responseByteBuf = Unpooled.copiedBuffer(response.getBytes());
		ctx.writeAndFlush(responseByteBuf);
		logger.info("server receive:{}, count:{}, send:{}", body, counter.incrementAndGet(), response);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable t) throws Exception {
		logger.error("exceptionCaught error==>>", t);
		ctx.close();
	}

}
