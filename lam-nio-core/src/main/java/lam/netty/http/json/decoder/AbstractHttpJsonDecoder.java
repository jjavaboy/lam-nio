package lam.netty.http.json.decoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2016年10月10日
* @versio 1.0
*/
public abstract class AbstractHttpJsonDecoder<T> extends MessageToMessageDecoder<T>{

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private Class<?> clazz;
	
	protected AbstractHttpJsonDecoder(Class<?> clazz){
		this.clazz = clazz;
	}
	
	protected Object decode0(ChannelHandlerContext ctx, ByteBuf body){
		String content = body.toString(CharsetUtil.UTF_8);
		return content;
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error(cause.getMessage());
		ctx.close();
	}
	
}
