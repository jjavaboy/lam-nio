package lam.mongo.enumer;
/**
* <p>
* 性别枚举
* </p>
* @author linanmiao
* @date 2016年10月28日
* @versio 1.0
*/
public enum Gender {
	
	man(1),
	
	female(2),
	
	unkown(0);
	
	private int value;
	
	private Gender(int value){
		this.value = value;
	}
	
	public int getValue(){
		return this.value;
	}

}
