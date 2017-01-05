package lam.mongo.demo;

import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.google.gson.Gson;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import lam.mongo.db.MongoDBClient;
import lam.mongo.enumer.Gender;
import lam.mongo.model.User;

/**
* <p>
* test class about mongodb
* </p>
* @author linanmiao
* @date 2016年10月26日
* @versio 1.0
*/
public class DemoTest {
	
	private static Gson gson = new Gson();
	
	public static void main(String[] args){
		MongoDBClient client = new MongoDBClient("192.168.204.127", 28017, "datacenter").init();
		
		userInsertTest(client);
		//userFindAllTest(client);
		//userFindTest(client);
		//userFindWithConditionTest(client);
		//userFindTest(client);
		//userUpdateTest(client);
		//userDeleteTest(client);
		//pushElementTest(client);
		
		client.close();
	}
	
	private static void userDeleteTest(MongoDBClient client) {
		MongoCollection<Document> coll = client.getCollection(User.class);
		
		DeleteResult rs = coll.deleteOne(new Document("_id", new ObjectId("581300dae8b9bfe336a6dad3")));
		System.out.println("rs:" + rs);
	}

	private static void userUpdateTest(MongoDBClient client) {
		User user = userFindTest(client);
		MongoCollection<Document> coll = client.getCollection(User.class);
		
		user.setId(3);
		user.setUsername("lam");
		user.setFullname(user.getFullname());
		user.setGender(Gender.unkown.getValue());
		UpdateResult rs = coll.replaceOne(new Document("_id", new ObjectId("581300dae8b9bfe336a6dad3")), Document.parse(gson.toJson(user)));
		System.out.println("rs:" + rs);
	}

	private static User userFindTest(MongoDBClient client) {
		MongoCollection<Document> coll = client.getCollection(User.class);
		FindIterable<Document> iter = coll.find(new Document("_id", new ObjectId("581300dae8b9bfe336a6dad3")));
		Document doc = iter.first();
		if(doc != null){
			User user = gson.fromJson(doc.toJson(), User.class);
			System.out.println("user:" + gson.toJson(user));
			return user;
		}
		return null;
	}

	private static void userFindWithConditionTest(MongoDBClient client) {
		MongoCollection<Document> coll = client.getCollection(User.class);
		//FindIterable<Document> iter = coll.find(Filters.eq("id", 1));
		FindIterable<Document> iter = coll.find(new Document("name", "aa"));
		printFindIterable(iter);
	}

	private static void userFindAllTest(MongoDBClient client) {
		MongoCollection<Document> coll = client.getCollection(User.class);
		FindIterable<Document> iter = coll.find();
		printFindIterable(iter);
	}
	
	private static void userInsertTest(MongoDBClient client){
		MongoCollection<Document> coll = client.getCollection(User.class);
		
		User user = new User();
		user.setId(6);
		user.setUsername("li");
		user.setFullname("超级");
		user.setGender(Gender.man.getValue());
		
		coll.insertOne(Document.parse(gson.toJson(user)));
	}
	
	private static void pushElementTest(MongoDBClient client){
		MongoCollection<Document> coll = client.getCollection(User.class);
		
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("name", "angel");
		Document d = new Document("classmate", m);
		
		coll.updateOne(new Document("id", 3), new Document("$push", d)); //, new UpdateOptions().upsert(true)
	}
	
	private static void printFindIterable(FindIterable<Document> iter){
		for(Document doc : iter){
			System.out.println(doc.toJson());
		}
	}
	
}
