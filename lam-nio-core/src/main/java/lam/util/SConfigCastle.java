package lam.util;

import java.util.Objects;
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
public abstract class SConfigCastle {
	
	private static final ConcurrentMap<String, Properties> CONFIG = new ConcurrentHashMap<String, Properties>();
	
	public abstract String getFile();
	
	public Properties getProperties(String filename){
		Properties p = CONFIG.get(filename);
		if(p == null){
			CONFIG.putIfAbsent(filename, FileUtil.getProperties(filename));
			p = CONFIG.get(filename);
		}
		return p;
	}
	
	/**
	 * get value via <code>System.getProperty(String key)</code> firstly, then <code>Properties.getProperty(String key)</code>.
	 * @param key
	 * @return
	 */
	public String getValue(String key){
		String value = System.getProperty(key);
		if(Strings.isNoneBlank(value))
			return value;
		
		Properties p = getProperties(getFile());
		Objects.requireNonNull(p, "file " + getFile() + " maybe not exists.");
		return p.getProperty(key);
	}
	
	public int getIntValue(String key){
		String value = getValue(key);
		return Integer.parseInt(value);
	}
	
	public long getLongValue(String key){
		String value = getValue(key);
		return Long.parseLong(value);
	}

}
