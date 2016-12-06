package lam.mongo.json.strategy;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import lam.mongo.json.exclusion.JsonExclusion;

/**
* <p>
* 
* </p>
* @author linanmiao
* @date 2016年12月6日
* @version 1.0
*/
public class JsonExclusionStrategy {
	
	//排除注解的属性
	public static ExclusionStrategy fieldExclustionStrategy = new ExclusionStrategy(){

		@Override
		public boolean shouldSkipClass(Class<?> arg0) {
			return false;
		}

		@Override
		public boolean shouldSkipField(FieldAttributes fieldAttributes) {
			boolean has = fieldAttributes.getAnnotation(JsonExclusion.class) != null;
			return has;
		}
		
	};

}
