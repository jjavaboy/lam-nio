package lam.util;

import com.google.gson.Gson;

/**
* <p>
* util class for gson.
* </p>
* @author linanmiao
* @date 2018年5月24日
* @version 1.0
*/
public class Gsons {
	
	private static Gson gson = new Gson();
	
	public static String toJson(Object src) {
		return gson.toJson(src);
	}
	
	public static <T> T from(String json, Class<T> classOfT) {
		return gson.fromJson(json, classOfT);
	}

}
