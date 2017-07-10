package lam.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年6月30日
* @version 1.0
*/
public class MapUtil {
	
	private static Gson gson = new Gson();
	
	public static Map<String, String> toString2StringMap(Object object){
		Map<String, String> map = new HashMap<String, String>();
		Class<?> clazz = object.getClass();
		try {
			for(Field field : clazz.getDeclaredFields()){
				if(Modifier.isAbstract(field.getModifiers()) || Modifier.isTransient(field.getModifiers())){
					continue ;
				}
				map.put(field.getName(), gson.toJson(field.get(object)));
			}
		} catch (IllegalArgumentException e) {
			throw e;
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		return map;
	}
	
	public static <T> T toObject(T t, Map<String, String> map){
		Class clazz = t.getClass();
		Object object;
		try {
			object = clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
		for(Field field : clazz.getDeclaredFields()){
			if(Modifier.isAbstract(field.getModifiers()) || Modifier.isTransient(field.getModifiers())){
				continue ;
			}
			//field.set(object, map.get(field.getName()));
			
		}
		return null;
	}

}
