package lam.dubbo.extension;
/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年3月3日
* @version 1.0
*/
public class Holder<T>{

	private volatile T value;
	
	public void set(T value){
		this.value = value;
	}
	
	public T get() {
		return value;
	}
	
}
