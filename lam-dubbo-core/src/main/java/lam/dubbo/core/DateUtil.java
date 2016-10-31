package lam.dubbo.core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
* <p>
* util class about date
* </p>
* @author linanmiao
* @date 2016年10月30日
* @versio 1.0
*/
public class DateUtil {
	
	public static final String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss SSS";
	
	public static String toString(Date date, String format){
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}
	
	public static String now(){
		return toString(new Date(), YYYY_MM_DD_HH_MM_SS_SSS);
	}

}
