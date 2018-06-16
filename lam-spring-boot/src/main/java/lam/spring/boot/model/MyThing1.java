package lam.spring.boot.model;
/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2018年6月16日
* @versio 1.0
*/
public class MyThing1 {
	
	private int id;
	
	private String name;
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public MyThing1 setId(int id) {
		this.id = id;
		return this;
	}
	
	public MyThing1 setName(String name) {
		this.name = name;
		return this;
	}
}
