package lam.netty.http.json.encoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2016年10月9日
* @versio 1.0
*/
public abstract class AbstractHttpJsonEncoder<T> extends MessageToMessageEncoder<T>{
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	protected ByteBuf encode0(ChannelHandlerContext ctx, Object body){
		String json = new Gson().toJson(body, body.getClass());
		ByteBuf byteBuf = Unpooled.copiedBuffer(json, CharsetUtil.UTF_8);
		return byteBuf;
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error(cause.getMessage());
		ctx.close();
	}

}
