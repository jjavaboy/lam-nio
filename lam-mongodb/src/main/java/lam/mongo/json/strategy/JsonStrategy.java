package lam.mongo.json.strategy;

import java.io.IOException;

import org.bson.types.ObjectId;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import lam.mongo.json.exclusion.JsonExclusion;

/**
* <p>
* 
* </p>
* @author linanmiao
* @date 2016年12月6日
* @version 1.0
*/
public class JsonStrategy {
	
	public static TypeAdapter<ObjectId> createObjectIdAdapter(){
		return new ObjectIdAdapter();
	}
	
	private static class ObjectIdAdapter extends TypeAdapter<ObjectId>{

		@Override
		public ObjectId read(JsonReader jsonReader) throws IOException {
			try{
				jsonReader.beginObject();
				while("$oid".equals(jsonReader.nextName())){
					return new ObjectId(jsonReader.nextString());
				}
				return null;
			}finally{
				jsonReader.endObject();
			}
		}

		@Override
		public void write(JsonWriter arg0, ObjectId arg1) throws IOException {
		}
		
	}
	
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
