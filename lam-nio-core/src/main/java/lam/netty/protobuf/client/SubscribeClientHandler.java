package lam.netty.protobuf.client;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lam.netty.protobuf.model.SubscribeReqProto;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2016年10月4日
* @versio 1.0
*/
public class SubscribeClientHandler extends ChannelInboundHandlerAdapter{

	private static Logger logger = LoggerFactory.getLogger(SubscribeClientHandler.class);
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		for(int i = 0; i < 10; i++){
			SubscribeReqProto.SubscribeReq r = req(i);
			logger.info("client send:\n" + r);
			ctx.write(r);
		}
		ctx.flush();
	}
	
	private SubscribeReqProto.SubscribeReq req(int subReqId){
		List<String> address = new ArrayList<String>();
		address.add("Beijing");
		address.add("shanghai");
		address.add("guangzhou");
		return 
		SubscribeReqProto.SubscribeReq.newBuilder()
		.setSubReqId(subReqId)
		.setUsername("lam")
		.setProductName("engineer of anmiaolin by " + subReqId)
		.addAllAddress(address)
		.build();
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		logger.info("client receive:" + msg);
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("exceptionCaught error==>>", cause);
		ctx.close();
	}
	
}
