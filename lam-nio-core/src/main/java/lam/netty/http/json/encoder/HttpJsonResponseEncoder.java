package lam.netty.http.json.encoder;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import lam.netty.http.json.model.HttpJsonResponse;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2016年10月10日
* @versio 1.0
*/
public class HttpJsonResponseEncoder extends AbstractHttpJsonEncoder<HttpJsonResponse>{

	@Override
	protected void encode(ChannelHandlerContext ctx, HttpJsonResponse msg, List<Object> out) throws Exception {
		ByteBuf byteBuf = encode0(ctx, msg.getBody());
		FullHttpResponse response = msg.getResponse();
		if(response == null){
			response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
		}else{
			response = new DefaultFullHttpResponse(msg.getResponse().protocolVersion(), msg.getResponse().status(), byteBuf);
		}
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
		HttpUtil.setContentLength(response, byteBuf.readableBytes());
		out.add(response);
	}

}
