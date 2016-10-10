package lam.netty.http.json.decoder;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import lam.netty.http.json.model.HttpJsonRequest;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2016年10月10日
* @versio 1.0
*/
public class HttpJsonRequestDecoder extends AbstractHttpJsonDecoder<FullHttpRequest>{
	
	public HttpJsonRequestDecoder(Class<?> clazz){
		super(clazz);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, FullHttpRequest msg, List<Object> out) throws Exception {
		if(!msg.decoderResult().isSuccess()){
			sendError(ctx, HttpResponseStatus.BAD_REQUEST);
			return ;
		}
		HttpJsonRequest request = new HttpJsonRequest(msg, decode0(ctx, msg.content()));
		out.add(request);
	}
	
	private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status){
		ByteBuf byteBuf = Unpooled.copiedBuffer("Failure:" + status.toString(), CharsetUtil.UTF_8);
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, byteBuf);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=utf-8");
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

}
