package lam.serialization.protobuf.protostuff;

import java.util.ArrayList;
import java.util.List;

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
		//default: -Dprotostuff.runtime.allow_null_array_element=false
		System.setProperty("protostuff.runtime.allow_null_array_element", Boolean.TRUE.toString());
		
		//protostuff uses io.protostuff.runtime.DefaultIdStrategy in default 
		//if system do not configure protostuff.runtime.id_strategy_factory,
		//otherwise, protostuff will use custom id_strategy_factory to create IdStrategy.
		
		//use reflection, value of system variable protostuff.runtime.use_sun_misc_unsafe is true in default.
		System.setProperty("protostuff.runtime.use_sun_misc_unsafe", Boolean.FALSE.toString());
		
		MyFoo foo = new MyFoo();
		foo.setId(2);
		foo.setName("sky");
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(null);
		foo.setList(list);
		foo.setInts(new int[]{1, 2});
		foo.setStrArray(new String[]{"ab", null});
		
		Console.println(foo);
		
		Schema<MyFoo> fooSchema = RuntimeSchema.getSchema(MyFoo.class);
		
		LinkedBuffer buffer = LinkedBuffer.allocate(512);
		
		byte[] bytes;
		try {
			bytes = ProtostuffIOUtil.toByteArray(foo, fooSchema, buffer);
		} finally {
			buffer.clear();
		}
		
		MyFoo deseriFoo = fooSchema.newMessage();

		ProtostuffIOUtil.mergeFrom(bytes, deseriFoo, fooSchema);
		
		Console.println(deseriFoo);

	}

}
