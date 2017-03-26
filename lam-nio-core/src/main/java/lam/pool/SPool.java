package lam.pool;

import java.io.Closeable;
import java.util.NoSuchElementException;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import lam.log.Console;
import lam.pool.support.SGenericObjectPool;

/**
* <p>
* pool
* </p>
* @author linanmiao
* @date 2017年3月17日
* @version 1.0
*/
public class SPool<T> implements Closeable{
	
	//protected GenericObjectPool<T> objectPool;
	protected SGenericObjectPool<T> objectPool;

	public SPool(final GenericObjectPoolConfig config, PooledObjectFactory<T> factory){
		initPool(config, factory);
	}
	
	@Override
	public void close(){
		closeObjectPool();
	}
	
	public boolean isClosed(){
		return this.objectPool.isClosed();
	}
	
	protected void closeObjectPool(){
		if(this.objectPool != null){
			try{
				this.objectPool.close();
			}catch(Exception e){
				throw new SSocketException("Occurs exception when closing pool", e);				
			}
		}
	}
	
	public void initPool(final GenericObjectPoolConfig config, PooledObjectFactory<T> factory){
		if(this.objectPool != null){
			closeObjectPool();
		}
		this.objectPool = new SGenericObjectPool<T>(factory, config);
	}
	
	public T getResource(){
		try {
			return this.objectPool.borrowObject();
		} catch (NoSuchElementException nse){//NoSochElementException extends RuntimeException
			throw new SSocketException("Could not get a resource from the pool", nse);
		} catch (Exception e) {
			throw new SSocketException("Could not get a resource from the pool", e);
		}
	}
	
	public void returnResource(final T t){
		if(t != null){
			returnResourceObject(t);
		}
	}
	
	public void returnBrokenResource(final T t){
		if(t != null){
			returnBrokenResourceObject(t);
		}
	}
	
	protected void returnResourceObject(final T t){
		if(t == null){
			Console.println("The resource is null, do not need to return it to pool");
			return ;
		}
		try{
			this.objectPool.returnObject(t);
		}catch(Exception e){
			throw new SSocketException("Cound not return the resource to the pool", e);
		}
	}
	
	protected void returnBrokenResourceObject(final T t){
		if(t == null){
			Console.println("The broken resource is null, do not need to return to pool");
			return ;
		}
		try{
			//make this resource t invalidate, useless.
			this.objectPool.invalidateObject(t);
		}catch(Exception e){
			throw new SSocketException("Cound not return the broken resource to the pool", e);
		}
	}
	
	public int getNumActive(){
		if(isPoolInactive()){
			return -1;
		}
		return this.objectPool.getNumActive();
	}
	
	public int getNumIdle(){
		if(isPoolInactive()){
			return -1;
		}
		return this.objectPool.getNumIdle();
	}
	
	public int getNumWaiters(){
		if(isPoolInactive()){
			return -1;
		}
		return this.objectPool.getNumWaiters();
	}
	
	public long getMaxBorrowWaitTimeMillis(){
		if(isPoolInactive()){
			return -1;
		}
		return this.objectPool.getMaxBorrowWaitTimeMillis();
	}
	
	public long getMeanBorrowWaitTimeMillis(){
		if(isPoolInactive()){
			return -1;
		}
		return this.objectPool.getMeanBorrowWaitTimeMillis();
	}
	
	public boolean isPoolInactive(){
		return this.objectPool == null || isClosed();
	}
	
	public void addObjects(int nObjects){
		try{
			for(int i = 0; i < nObjects; i++){
				this.objectPool.addObject();
			}
		}catch(Exception e){
			throw new SSocketException("Occurs exception when add object to the pool", e);
		}
	}

}
