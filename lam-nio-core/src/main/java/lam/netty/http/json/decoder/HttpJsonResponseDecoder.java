package lam.netty.http.json.decoder;

import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import lam.netty.http.json.model.HttpJsonResponse;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2016年10月10日
* @versio 1.0
*/
public class HttpJsonResponseDecoder extends AbstractHttpJsonDecoder<DefaultFullHttpResponse>{
	
	public HttpJsonResponseDecoder(Class<?> clazz) {
		super(clazz);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, DefaultFullHttpResponse msg, List<Object> out) throws Exception {
		Object body = decode0(ctx, msg.content());
		HttpJsonResponse response = new HttpJsonResponse(msg, body);
		out.add(response);
	}


}
