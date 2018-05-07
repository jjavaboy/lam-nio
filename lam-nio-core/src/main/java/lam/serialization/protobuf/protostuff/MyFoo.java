package lam.serialization.protobuf.protostuff;

import java.util.concurrent.atomic.AtomicLong;

import io.protostuff.Exclude;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2018年5月6日
* @versio 1.0
*/
public class MyFoo {
	
	private static final AtomicLong OBJECT_ID_POOL = new AtomicLong(0); 
	
	//protostuff排除序列化的属性
	@Exclude
	private final long objectId;
	
	private int id;
	
	private String name;
	
	public MyFoo() {
		this.objectId = OBJECT_ID_POOL.getAndIncrement();
	}
	
	public long getObjectId() {
		return objectId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
