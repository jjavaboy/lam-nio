package lam.distribution;

import java.io.Closeable;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年8月24日
* @version 1.0
*/
public interface LRegistry extends Closeable{
	
	public boolean doRegistry();
	
	public void subcribe(NotifyListener notify);
	
	public static interface NotifyListener{
		public void listen();
	}

}
