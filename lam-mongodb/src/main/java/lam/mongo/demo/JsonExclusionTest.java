package lam.mongo.demo;

import org.bson.Document;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.FindIterable;
import com.sun.corba.se.spi.ior.ObjectId;

import lam.mongo.db.MongoDBClient;
import lam.mongo.json.strategy.JsonStrategy;
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
	
	private static Gson gson = new GsonBuilder()//.registerTypeAdapter(ObjectId.class, JsonStrategy.createObjectIdAdapter())
			.setExclusionStrategies(JsonStrategy.fieldExclustionStrategy).create();

	public static void main(String[] args){
		MongoDBClient client = new MongoDBClient("192.168.204.127", 28017, "test").init();
		
		//insertTest(client);
		findRoleTest(client);
		
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
	
	public static void findRoleTest(MongoDBClient client){
		FindIterable<Document> iter = client.getCollection(Role.class).find(new Document("id", 2));
		if(iter != null){
			System.out.println(iter.first());
		}
	}
	
}
