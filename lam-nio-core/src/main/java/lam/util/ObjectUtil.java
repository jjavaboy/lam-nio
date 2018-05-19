package lam.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.google.gson.Gson;

import lam.serialization.protobuf.protostuff.MyFoo;
import lam.serialization.protobuf.protostuff.MyFoo2;

/**
* <p>
* Some tool for Object.
* </p>
* @author linanmiao
* @date 2018年5月18日
* @version 1.0
*/
public class ObjectUtil {
	
	public static void cloneField(Object from, Object to) {
		Objects.requireNonNull(from, "param:from can not be null");
		Objects.requireNonNull(to, "param:to can not be null");
		
		Map<String, Field> fromFieldMap = new HashMap<String, Field>();
		mapField(from.getClass(), fromFieldMap);
		
		Field[] toFields = to.getClass().getDeclaredFields();
		cloneField(from, fromFieldMap, to, to.getClass(), toFields);

	}
	
	private static void cloneField(Object from, Map<String, Field> fromFieldMap, Object to, Class<?> toClass, Field[] toFields) {

		for (Field field : toFields) {
			if (Modifier.isStatic(field.getModifiers()) ||
				/*Modifier.isFinal(field.getModifiers()) ||*/
				Modifier.isTransient(field.getModifiers())) {
				continue ;
			}
			Field fromField = fromFieldMap.get(field.getName());
			if (fromField == null || fromField.getType() != field.getType()) {
				continue ;
			}
			if (!fromField.isAccessible()) {
				fromField.setAccessible(true);
			}
			Object value = null;
			try {
				value = fromField.get(from);
			} catch (IllegalArgumentException e1) {
				throw e1;
			} catch (IllegalAccessException e1) {
				throw new RuntimeException(e1);
			}
			try {
				if (!field.isAccessible()) {
					field.setAccessible(true);
				}
				field.set(to, value);
			} catch (IllegalArgumentException e) {
				throw e;
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		
		//clone super class fields
		Class<?> superClass = toClass.getSuperclass();
		if (superClass != null && superClass != Object.class) {
			Field[] superFileds = superClass.getDeclaredFields();
			cloneField(from, fromFieldMap, to, superClass, superFileds);
		}
	}
	
	private static void mapField(Class<?> clazz, Map<String, Field> map) {
		//This includes public, protected, default (package) access, and private fields, 
		//but excludes inherited fields.
		// The elements in the array returned are not sorted and are not in any particular order.
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			/**
		    private static final int FIELD_MODIFIERS =
		        Modifier.PUBLIC         | Modifier.PROTECTED    | Modifier.PRIVATE |
		        Modifier.STATIC         | Modifier.FINAL        | Modifier.TRANSIENT |
		        Modifier.VOLATILE;
			 */
			if (Modifier.isStatic(field.getModifiers()) || 
				/*Modifier.isFinal(field.getModifiers()) || */
				Modifier.isTransient(field.getModifiers())) {
				continue ;
			}
			if (!map.containsKey(field.getName())) {
				map.put(field.getName(), field);
			}
		}
		Class<?> superClass = clazz.getSuperclass();
		if (superClass != null && superClass != Object.class) {
			mapField(superClass, map);
		}
	}
	
	public static void main(String[] args) {
		Gson gson = new Gson();
		
		MyFoo myFoo = new MyFoo();
		
		MyFoo myFoo0 = new MyFoo();
		myFoo0.setId(1);
		myFoo0.setName("wuman");
		//System.out.println(gson.toJson(myFoo0));
		
		MyFoo2 myFoo2 = new MyFoo2();
		cloneField(myFoo0, myFoo2);
		
		//System.out.println(gson.toJson(myFoo2));
		
		System.out.println("=============");
	}

}
