package lam.netty.http.json.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lam.netty.http.json.model.HttpJsonRequest;
import lam.netty.http.json.model.HttpJsonResponse;
import lam.netty.http.json.model.Order;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2016年10月10日
* @versio 1.0
*/
public class HttpJsonServerHandler extends SimpleChannelInboundHandler<HttpJsonRequest>{
	
	private static Logger logger = LoggerFactory.getLogger(HttpJsonServerHandler.class);

	@Override
	protected void channelRead0(final ChannelHandlerContext ctx, HttpJsonRequest msg) throws Exception {
		FullHttpRequest request = msg.getRequest();
		Order order = (Order) msg.getBody();
		doBusiness(order);
		HttpJsonResponse response = new HttpJsonResponse(null, order);
		ChannelFuture f = ctx.writeAndFlush(response);
		if(!HttpUtil.isKeepAlive(request)){
			f.addListener(new GenericFutureListener<Future<? super Void>>(){
				@Override
				public void operationComplete(Future<? super Void> future) throws Exception {
					ctx.close();
				}
			});
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("error==>>", cause);
		ctx.close();
	}
	
	private void doBusiness(Order order){
		logger.info("server doBusiness, order:" + order);
	}

}
