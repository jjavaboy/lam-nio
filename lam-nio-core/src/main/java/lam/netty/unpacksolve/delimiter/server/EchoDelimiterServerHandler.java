package lam.netty.unpacksolve.delimiter.server;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
* <p>
* uses delimiter to control each message
* </p>
* @author linanmiao
* @date 2016年10月3日
* @versio 1.0
*/
public class EchoDelimiterServerHandler extends ChannelInboundHandlerAdapter{
	
	private static Logger logger = LoggerFactory.getLogger(EchoDelimiterServerHandler.class);
	
	private String delimiter;

	private AtomicInteger counter;
	
	public EchoDelimiterServerHandler(String delimiter) {
		this.delimiter = delimiter;
		this.counter = new AtomicInteger(0);
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String request = (String) msg;
		String response = String.format("%s has been received by server terminal%s", request, delimiter);
		ByteBuf respByteBuf = Unpooled.copiedBuffer(response.getBytes());
		ctx.writeAndFlush(respByteBuf);
		logger.info("server receive:{}, count:{}", request, counter.incrementAndGet());
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("exceptionCaught print error==>>", cause);
		ctx.close();
	}

}
