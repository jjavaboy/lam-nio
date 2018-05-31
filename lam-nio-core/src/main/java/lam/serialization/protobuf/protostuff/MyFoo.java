package lam.serialization.protobuf.protostuff;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import io.protostuff.Exclude;
import io.protostuff.Tag;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2018年5月6日
* @versio 1.0
*/
public class MyFoo extends SuperFoo{
	
	private static final AtomicLong OBJECT_ID_POOL = new AtomicLong(0);
	
	//protostuff排除序列化的属性
	@Exclude
	private final long objectId;
	
	//protostuff排除序列化的属性
	@Deprecated
	private long oldId;
	
	@Tag(value = 1, alias = "id")
	private int id;
	
	@Tag(value = 2, alias = "name")
	private String name;
	
	@Tag(value = 3)
	private List<Integer> list;
	
	@Tag(value = 4)
	private int[] ints;
	
	@Tag(value = 5)
	private String[] strArray;
	
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
	
	public void setList(List<Integer> list) {
		this.list = list;
	}
	
	public List<Integer> getList() {
		return list;
	}
	
	public void setInts(int[] ints) {
		this.ints = ints;
	}
	
	public int[] getInts() {
		return ints;
	}
	
	public void setStrArray(String[] strArray) {
		this.strArray = strArray;
	}
	
	public String[] getStrArray() {
		return strArray;
	}
	
}
