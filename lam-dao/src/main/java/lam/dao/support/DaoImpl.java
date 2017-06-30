package lam.dao.support;
/**
* <p>
* DB dao
* </p>
* @author linanmiao
* @date 2017年6月30日
* @version 1.0
*/
public class DaoImpl<T> implements Dao<T>{
	
	private CacheDao<T> cacheDao;
	
	private Class<T> clazz;
	
	public void setCacheDao(CacheDao<T> cacheDao) {
		this.cacheDao = cacheDao;
	}
	
	public DaoImpl(Class<T> clazz){
		this.clazz = clazz;
	}
	
	@Override
	public T add(T t) {
		//to set uid value of t
		//...
		try{
			boolean rt = cacheDao.set(t);
			if(!rt){
			//make a mark:cache is fail
			//...
			}
		}catch(Exception e){
			e.printStackTrace();
			//make a mark:cache is fail
			//...
		}
		return t;
	}

	@Override
	public T get(Long id) {
		T t = null;
		try{
			t = cacheDao.get(id);
		}catch(Exception e){
			e.printStackTrace();
		}
		return t;
	}

	@Override
	public boolean update(T t) {
		boolean rt = false;
		try{
			rt = cacheDao.set(t);
			if(!rt){
			//make a mark:cache is fail
			//...
			}
		}catch(Exception e){
			e.printStackTrace();
			//make a mark:cache is fail
			//...
		}
		return rt;
	}

	@Override
	public boolean delete(Long id) {
		return cacheDao.delete(id);
	}

}
