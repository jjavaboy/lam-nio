package lam.netty.http.json.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lam.netty.http.json.factory.OrderFactory;
import lam.netty.http.json.model.HttpJsonRequest;
import lam.netty.http.json.model.HttpJsonResponse;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2016年10月10日
* @versio 1.0
*/
public class HttpJsonClientHandler extends SimpleChannelInboundHandler<HttpJsonResponse>{
	
	private static Logger logger = LoggerFactory.getLogger(HttpJsonClientHandler.class);
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		HttpJsonRequest request = new HttpJsonRequest(null, OrderFactory.create(1));
		ctx.writeAndFlush(request);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HttpJsonResponse msg) throws Exception {
		logger.info("client receive header of http response:" + msg.getResponse().headers().names());
		logger.info("client receive body of http response:" + msg.getBody());
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("error==>>", cause);
		ctx.close();
	}

}
