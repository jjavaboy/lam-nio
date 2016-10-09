package lam.netty.http.json.encoder;

import java.net.InetAddress;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import lam.netty.http.json.model.HttpJsonRequest;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2016年10月9日
* @versio 1.0
*/
public class HttpJsonRequestEncoder extends AbstractHttpJsonEncoder<HttpJsonRequest>{

	@Override
	protected void encode(ChannelHandlerContext ctx, HttpJsonRequest msg, List<Object> out) throws Exception {
		ByteBuf byteBuf = encode0(ctx, msg.getBody());
		FullHttpRequest request = msg.getRequest();
		if(request == null){
			request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/do", byteBuf);
			request.headers().set(HttpHeaderNames.HOST, InetAddress.getLocalHost().getHostAddress());
			request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
			request.headers().set(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP_DEFLATE);
			request.headers().set(HttpHeaderNames.ACCEPT_CHARSET, "ISO-8859-1,UTF-8;q=0.7,*;q=0.7");
			request.headers().set(HttpHeaderNames.ACCEPT_LANGUAGE, "zh");
			request.headers().set(HttpHeaderNames.USER_AGENT, "Netty json Http Client");
			request.headers().set(HttpHeaderNames.ACCEPT, 
					"text/html,application/xhtml+xml,application/xml,application/json;q=0.9,*/*;q=0.8");
			HttpUtil.setContentLength(request, byteBuf.readableBytes());
			out.add(request);
		}
	}

}
