package lam.serialization.protobuf.protostuff;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import lam.log.Console;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2018年5月6日
* @versio 1.0
*/
public class ProtostuffTest {
	
	public static void main(String[] args) {
		MyFoo foo = new MyFoo();
		foo.setId(2);
		foo.setName("sky");
		
		Schema<MyFoo> fooSchema = RuntimeSchema.getSchema(MyFoo.class);
		
		LinkedBuffer buffer = LinkedBuffer.allocate(512);
		
		byte[] bytes;
		try {
			bytes = ProtostuffIOUtil.toByteArray(foo, fooSchema, buffer);
		} finally {
			buffer.clear();
		}
		
		MyFoo deseriFoo = fooSchema.newMessage();
		
		Console.println(deseriFoo);

		ProtostuffIOUtil.mergeFrom(bytes, deseriFoo, fooSchema);
		
		Console.println(deseriFoo);
	}

}
