package lam.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
* <p>
* 日期工具类
* </p>
* @author linanmiao
* @date 2016年9月28日
* @versio 1.0
*/
public class DateUtil {
	
	public static final String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

	public static final String FORMAT_YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss SSS";
	
	public static String getCurrentTimeSSS(){
		return getCurrentTime(FORMAT_YYYY_MM_DD_HH_MM_SS_SSS);
	}
	
	public static String getCurrentTime(){
		return getCurrentTime(FORMAT_YYYY_MM_DD_HH_MM_SS);
	}
	
	public static String getCurrentTime(String format){
		return getCurrentTime(new Date(), format);
	}
	
	public static String getCurrentTime(Date date, String format){
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}

}
