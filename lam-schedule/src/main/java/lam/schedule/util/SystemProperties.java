package lam.schedule.util;

import java.io.File;

import lam.schedule.constant.Constant;

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
		String defaultValue = getDefaultValue(key);
		return System.getProperty(key, defaultValue);
	}
	
	public static int getPropertyInt(String key){
		try{
			return Integer.parseInt(getProperty(key));
		}catch(NumberFormatException e){
			return -1;
		}
	}
	
	public static String getDefaultValue(String key){
		if(Constant.JVM_CM_CACHE_FILE.equals(key)){
			return System.getProperty("user.home") + File.separator + "lam-schedule.cache.properties";
		}else if(Constant.JVM_CM_CONTAINER.equals(key)){
			return "filecache";
		}else if(Constant.JVM_CM_RPC_PORT.equals(key)){
			return "6666";
		}else if(Constant.JVM_CM_SCHEDULE_IN_ADVANCE_HOUR.equals(key)){
			return "2";
		}else if(Constant.JVM_CM_SCHEDULE_TEST.equals(key)){
			return Boolean.FALSE.toString();
		}
		return null;
	}
	
}
