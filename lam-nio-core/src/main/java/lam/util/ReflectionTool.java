package lam.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
* <p>
* reflection tool
* </p>
* @author linanmiao
* @date 2017年5月31日
* @version 1.0
*/
public class ReflectionTool {
	
	public static String toString(Object object){
		if(object == null){
			return "null";
		}
		Class<?> clazz = object.getClass();
		/*
		 * eight primitive type(boolean, byte, short, char, int, long, float, double) and void type
		 */
		if(clazz.isPrimitive()){
			return object.toString();
		}
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		Field[] fields = clazz.getDeclaredFields();
		boolean hasValuePreviously = Boolean.FALSE.booleanValue();
		for(int index = 0, size = fields.length; index < size ; index++){
			Field field = fields[index];
			if(Modifier.isStatic(field.getModifiers())){
				continue ;
			}
			if(!field.isAccessible()){
				field.setAccessible(Boolean.TRUE.booleanValue());
			}
			Object value = null;
			try {
				value = field.get(object);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			if(value != null){
				if(hasValuePreviously){
					sb.append(", ");
				}
				hasValuePreviously = Boolean.TRUE.booleanValue();
				sb.append("\"").append(field.getName()).append("\"").append(":").append(value);
			}
		}
		sb.append("}");
		return sb.toString();
	}
}
