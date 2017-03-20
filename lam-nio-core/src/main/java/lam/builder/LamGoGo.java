package lam.builder;

import com.google.gson.Gson;

/**
* <p>
* 使用构建器来创建对象
* </p>
* @author linanmiao
* @date 2017年3月21日
* @version 1.0
*/
public class LamGoGo {
	
	private int id;
	private String name;
	private int age;
	private String address;
	private int gender;
	
	public LamGoGo(Builder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.age = builder.age;
		this.address = builder.address;
		this.gender = builder.gender;
	}
	
	/**
	 * 构建器
	 */
	public static class Builder{
		private int id;
		private String name;
		private int age;
		private String address;
		private int gender;
		
		public Builder setId(int id) {
			this.id = id;
			return this;
		}
		
		public Builder setName(String name) {
			this.name = name;
			return this;
		}
		
		public Builder setAge(int age) {
			this.age = age;
			return this;
		}
		
		public Builder setAddress(String address) {
			this.address = address;
			return this;
		}
		
		public Builder setGender(int gender) {
			this.gender = gender;
			return this;
		}
		
		public LamGoGo builder(){
			return new LamGoGo(this);
		}
	}
	
	public static void main(String[] args){
		LamGoGo lamGoGo = new LamGoGo.Builder()
		.setId(1)
		.setName("lam")
		.setAge(18)
		.setGender(2)
		.setAddress("china")
		.builder();
		System.out.println(new Gson().toJson(lamGoGo));
	}

}
