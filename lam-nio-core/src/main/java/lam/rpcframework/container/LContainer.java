package lam.rpcframework.container;

import java.io.Closeable;

/**
* <p>
* container
* </p>
* @author linanmiao
* @date 2017年5月11日
* @version 1.0
*/
public interface LContainer extends Closeable{
	
	public void start();
	
	public void close();

}
