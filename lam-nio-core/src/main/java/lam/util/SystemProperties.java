package lam.util;

/**
* <p>
* system properties
* </p>
* @author linanmiao
* @date 2017年5月12日
* @version 1.0
*/
public class SystemProperties {

	public static String getProperty(String key){
		return System.getProperty(key);
	}
	
	public static int getPropertyInt(String key){
		try{
			return Integer.parseInt(getProperty(key));
		}catch(NumberFormatException e){
			throw e;
		}
	}
	
	public static boolean getPropertyBoolean(String key) {
		return Boolean.parseBoolean(getProperty(key));
	}
	
}
