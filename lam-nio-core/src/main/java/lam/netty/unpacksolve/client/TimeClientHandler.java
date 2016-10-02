package lam.netty.unpacksolve.client;

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
public class TimeClientHandler extends ChannelInboundHandlerAdapter{
	
	private static Logger logger = LoggerFactory.getLogger(TimeClientHandler.class);
	
	private AtomicInteger counter = new AtomicInteger(0);
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		String request = "QUERY TIME" + System.getProperty("line.separator");
		byte[] bytes = request.getBytes();
		ByteBuf requestByteBuf = null;
		for(int i = 0; i < 100; i++){
			requestByteBuf = Unpooled.buffer(bytes.length);
			requestByteBuf.writeBytes(bytes);
			ctx.writeAndFlush(requestByteBuf);
		}
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String body = (String)msg;
		logger.info("client receive:{}, count:{}", body, counter.incrementAndGet());
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable t) throws Exception {
		logger.error("exceptionCaught error==>>", t);
		ctx.close();
	}

}
