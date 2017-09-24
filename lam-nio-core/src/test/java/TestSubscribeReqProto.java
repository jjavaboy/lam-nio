import java.util.ArrayList;
import java.util.List;

import com.google.protobuf.InvalidProtocolBufferException;

import lam.netty.protobuf.model.SubscribeReqProto;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2016年10月4日
* @versio 1.0
*/
public class TestSubscribeReqProto {
	
	private static byte[] encode(SubscribeReqProto.SubscribeReq req){
		return req.toByteArray();
	}
	
	private static SubscribeReqProto.SubscribeReq decode(byte[] body) 
			throws InvalidProtocolBufferException{
		return SubscribeReqProto.SubscribeReq.parseFrom(body);
	}
	
	private static SubscribeReqProto.SubscribeReq createSubscribeReq(){
		SubscribeReqProto.SubscribeReq.Builder builder =
				SubscribeReqProto.SubscribeReq.newBuilder();
		builder.setSubReqId(1);
		builder.setUsername("anmiaolin");
		builder.setProductName("engineer");
		List<String> address = new ArrayList<String>();
		address.add("Beijing");
		address.add("shanghai");
		address.add("guangzhou");
		builder.addAllAddress(address);
		return builder.build();
	}
	
	public static void main(String[] args) 
			throws InvalidProtocolBufferException{
		SubscribeReqProto.SubscribeReq req = createSubscribeReq();
		System.out.println("Before encode:\n" + req.toString());
		SubscribeReqProto.SubscribeReq req2 = decode(encode(req));
		System.out.println("After decode:\n" + req.toString());
		System.out.println("Assert equal:" + req2.equals(req));
	}

}
