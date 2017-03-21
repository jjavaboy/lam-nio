package lam.pool;
/**
* <p>
* pooling
* </p>
* @author linanmiao
* @date 2017年3月21日
* @versio 1.0
*/
public interface Pooling <T>{
	
	public T getResource();
	
	public void returnResource(T t);
	
	public void returnBrokenResource(T t);

}
