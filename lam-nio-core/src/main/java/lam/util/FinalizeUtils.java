package lam.util;

import java.io.Closeable;

/**
* <p>
* io util
* </p>
* @author linanmiao
* @date 2017年3月22日
* @version 1.0
*/
public class FinalizeUtils {
	
	/**
	 * close closeable instance quietly ignore exception.
	 * @param closeable
	 */
	public static void closeQuietly(Closeable closeable){
		close(closeable, true);
	}
	
	public static void closeNotQuietly(Closeable closeable){
		close(closeable, false);
	}
	
	private static void close(Closeable closeable, boolean ignoreException){
		if(closeable != null){
			try{
				closeable.close();
			}catch(Exception e){
				if(!ignoreException){
					e.printStackTrace();
				}
			}
		}
	}

}
