package lam.dao.support;

import lam.dao.support.model.CacheBean;

/**
* <p>
* Dao-处理雪崩
* </p>
* @author linanmiao
* @date 2017年6月29日
* @version 1.0
*/
public interface CacheDao <T extends CacheBean>{
	
	public boolean add(T t);
	
	public T get(Integer id);
	
	public boolean update(T t);
	
	public boolean delete(T t);

}
