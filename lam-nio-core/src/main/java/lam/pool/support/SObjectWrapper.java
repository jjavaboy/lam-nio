package lam.pool.support;
/**
* <p>
* wrapper class
* </p>
* @author linanmiao
* @date 2017年3月24日
* @version 1.0
*/
public class SObjectWrapper<T>{

	private final T object;
	
	public SObjectWrapper(T o){
		this.object = o;
	}
	
	public T getObject() {
		return object;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof SObjectWrapper && ((SObjectWrapper)obj).object == this.object;
	}
	
	@Override
	public int hashCode() {
		return System.identityHashCode(object);
	}
	
}
