package lam.netty.unpacksolve.fixedlength.server;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
* <p>
* 长度控制的server handler
* </p>
* @author linanmiao
* @date 2016年10月3日
* @versio 1.0
*/
public class FixedLengthServerHandler extends ChannelInboundHandlerAdapter{
	
	private static Logger logger = LoggerFactory.getLogger(FixedLengthServerHandler.class);
	
	private AtomicInteger counter = new AtomicInteger(0);
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		java.net.InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
		logger.info("connect {}:{}", inetSocketAddress.getHostName(), inetSocketAddress.getPort());
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String response = (String) msg;
		if(response != null && "quit".equals(response.trim())){
			ctx.close();
		}
		logger.info("server receive:{}, count:{}", response, counter.incrementAndGet() );
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable t) throws Exception {
		logger.error("exceptionCaught error==>>", t);
		ctx.close();
	}

}
