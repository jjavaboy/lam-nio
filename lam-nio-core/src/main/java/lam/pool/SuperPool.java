package lam.pool;

import java.io.Closeable;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
* <p>
* pool
* </p>
* @author linanmiao
* @date 2017年3月17日
* @version 1.0
*/
public class SuperPool<T> implements Closeable{
	
	protected GenericObjectPool<T> objectPool;

	@Override
	public void close(){
		//close work
	}
	
	public void closePool(){
		try{
			objectPool.close();
		}catch(Exception e){
			throw new RuntimeException("Occurs exception where closing pool", e);
		}
	}
	
	public void initPool(final GenericObjectPoolConfig config, PooledObjectFactory<T> factory){
		if(this.objectPool != null){
			closePool();
		}
		this.objectPool = new GenericObjectPool<T>(factory, config);
	}

}
