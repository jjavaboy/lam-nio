package lam.mongo.demo;

import org.bson.Document;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lam.mongo.db.MongoDBClient;
import lam.mongo.json.strategy.JsonExclusionStrategy;
import lam.mongo.model.Role;

/**
* <p>
* xx
* </p>
* @author linanmiao
* @date 2016年12月6日
* @version 1.0
*/
public class JsonExclusionTest {
	
	private static Gson gson = new GsonBuilder().setExclusionStrategies(JsonExclusionStrategy.fieldExclustionStrategy).create();

	public static void main(String[] args){
		MongoDBClient client = new MongoDBClient("192.168.204.127", 28017, "test").init();
		
		insertTest(client);
		
		client.close();
	}
	
	private static void insertTest(MongoDBClient client){
		Role role = new Role();
		role.setId(2);
		role.setKey("updateUser");
		role.setRolename("更新用户");
		role.setTemporaryName("更新用户专用角色");
		role.setCreateTime(new java.util.Date());
		role.setUpdateTime(role.getCreateTime());
		
		client.getCollection(Role.class).insertOne(Document.parse(gson.toJson(role)));;
	}
	
}
