package lam.netty.unpacksolve.delimiter.client;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2016年10月3日
* @versio 1.0
*/
public class EchoDelimiterClientHandler extends ChannelInboundHandlerAdapter{
	
	private static Logger logger = LoggerFactory.getLogger(EchoDelimiterClientHandler.class);

	private AtomicInteger counter;
	
	private String delimiter;
	
	public EchoDelimiterClientHandler(String delimiter) {
		this.delimiter = delimiter;
		this.counter = new AtomicInteger(0);
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		for(int i = 0; i < 10; i++){
			String request = "Hi, anmiaolin. Wilcome to netty" + (i + 1) + delimiter;
			ctx.writeAndFlush(Unpooled.copiedBuffer(request.getBytes()));
		}
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String response = (String) msg;
		logger.info("client receive:{}, count:{}", response, counter.incrementAndGet());
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable t) throws Exception {
		logger.error("exceptionCaught error==>>", t);
		ctx.close();
	}
	
}
