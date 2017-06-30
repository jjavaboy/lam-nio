package lam.dao.support;
/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年6月30日
* @version 1.0
*/
public interface Dao <T>{
	
	public T add(T t);
	
	public T get(Long id);
	
	public boolean update(T t);
	
	public boolean delete(Long id);

}
