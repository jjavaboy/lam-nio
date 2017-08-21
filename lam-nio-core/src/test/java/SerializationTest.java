import java.util.Arrays;

import com.google.gson.Gson;
import com.google.protobuf.InvalidProtocolBufferException;

import lam.serialization.protobuf.FooProto;
import lam.serialization.protobuf.HelloProto;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年6月7日
* @version 1.0
*/
public class SerializationTest {
	
	public static void main(String[] args){
		FooProto.Foo.Builder buidler = FooProto.Foo.newBuilder();
		
		buidler.putMapField(1, "ONE").putMapField(2, "SECOND").putMapField(0, "ZERO");
		
		FooProto.Foo foo = buidler.build();
		
		byte[] data = foo.toByteArray();
		
		try {
			System.out.println("length of byte[]:" + data.length);
			for(int i = 0, length = data.length; i < length; i++){
				byte b = data[i];
				System.out.println(i + ":" + Integer.toString(b, 16).toUpperCase() + "=====" + (char) b);
			}
			
			System.out.println("==============");
			
			FooProto.Foo foo_ = FooProto.Foo.parseFrom(data);
			System.out.print(new Gson().toJson(foo_));
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		
		System.out.println("\n" + Integer.toBinaryString(Integer.parseInt("12", 16)));
		
		HelloProto.Hello hello = HelloProto.Hello.newBuilder().setKk(-1).setBb(-2)/*.setGender(1)*/.build();
		byte[] bytes = hello.toByteArray();
		System.out.println("=============================length:" + bytes.length);
		for(int k = 0, len = bytes.length; k < len; k++){
			byte b = bytes[k];
			System.out.println(k + ":" + Integer.toString(b, 16).toUpperCase() + "(16)========" + Integer.toBinaryString(b) + "(2)");
		}
		
		System.out.println();
	}

}
