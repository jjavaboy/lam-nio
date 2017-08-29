package lam.util;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年8月29日
* @version 1.0
*/
public class SConfigCastle {
	
	private static final ConcurrentMap<String, Properties> CONFIG = new ConcurrentHashMap<String, Properties>();
	
	public static Properties getProperties(String filename){
		Properties p = CONFIG.get(filename);
		if(p == null){
			CONFIG.putIfAbsent(filename, FileUtil.getProperties(filename));
			p = CONFIG.get(filename);
		}
		return p;
	}
	
	/**
	 * get value via <code>System.getProperty(String key)</code> firstly, then <code>Properties.getProperty(String key)</code>.
	 * @param p
	 * @param key
	 * @return
	 */
	public static String getValue(Properties p, String key){
		String value = System.getProperty(key);
		if(Strings.isNoneBlank(value))
			return value;
		return p.getProperty(key);
	}

}
