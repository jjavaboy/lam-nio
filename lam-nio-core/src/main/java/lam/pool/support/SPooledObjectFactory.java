package lam.pool.support;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年3月22日
* @versio 1.0
*/
public abstract class SPooledObjectFactory<T> /*implements PooledObjectFactory<T>*/{
	
	public abstract SPooledObject<T> makeSObject() throws Exception;
	
	public abstract void destroySObject(SPooledObject<T> p) throws Exception;
	
	public abstract boolean validateSObject(SPooledObject<T> p);
	
	public abstract void activateSObject(SPooledObject<T> p) throws Exception;
	
	public abstract void passivateSObject(SPooledObject<T> p) throws Exception;
	
	/*@Override
	public PooledObject<T> makeObject() throws Exception {
		//return this.makeSObject();
		return null;
	}
	
	@Override
	public void destroyObject(PooledObject<T> p) throws Exception {
		this.destroySObject((SPooledObject<T>) p);
	}
	
	@Override
	public boolean validateObject(PooledObject<T> p) {
		return this.validateSObject((SPooledObject<T>) p);
	}
	
	@Override
	public void activateObject(PooledObject<T> p) throws Exception {
		this.activateSObject((SPooledObject<T>) p);
	}
	
	@Override
	public void passivateObject(PooledObject<T> p) throws Exception {
		this.passivateSObject((SPooledObject<T>) p);
	}*/

}
