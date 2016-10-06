package lam.netty.protobuf.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lam.netty.protobuf.model.SubscribeReqProto;
import lam.netty.protobuf.model.SubscribeRespProto;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2016年10月4日
* @versio 1.0
*/
public class SubscribeServerHandler extends ChannelInboundHandlerAdapter{
	
	private static Logger logger = LoggerFactory.getLogger(SubscribeServerHandler.class);
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		SubscribeReqProto.SubscribeReq req = (SubscribeReqProto.SubscribeReq) msg;
		
		if("anmiaolin".equals(req.getUsername())){
			logger.info("server receice subscribe:" + req);
			ctx.writeAndFlush(resp(req.getSubReqId()));
		}
	}
	
	public SubscribeRespProto.SubscribeResp resp(int subReqId){
		SubscribeRespProto.SubscribeResp.Builder builder =
				SubscribeRespProto.SubscribeResp.newBuilder();
		builder.setSubReqId(subReqId);
		builder.setRespCode(200);
		builder.setDesc("handle success, it will send to the address 3 days later.");
		return builder.build();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable t) throws Exception {
		logger.info("exceptionCaught error==>>", t);
		ctx.close();
	}

}
