package lam.log;

import lam.util.DateUtil;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年2月7日
* @versio 1.0
*/
public class MyLog {
	
	public static String timeAppend(String log){
		return String.format("%s:%s", DateUtil.getCurrentTimeSSS(), log);
	}

}
