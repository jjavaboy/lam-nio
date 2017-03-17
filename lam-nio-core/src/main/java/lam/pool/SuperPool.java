package lam.pool;

import java.io.Closeable;

import org.apache.commons.pool2.impl.GenericObjectPool;

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

}
