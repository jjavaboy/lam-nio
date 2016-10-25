package lam.mongo.demo;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
* <p>
* test class about mongodb
* </p>
* @author linanmiao
* @date 2016年10月26日
* @versio 1.0
*/
public class DemoTest {
	
	public static void main(String[] args){
		MongoClientURI connectionString = new MongoClientURI("mongodb://test:test@192.168.20.110:28017/?authSource=test");
		MongoClient mongoClient = new MongoClient(connectionString);
		//MongoClient mongoClient = new MongoClient("192.168.20.110", 28017);
		MongoDatabase database = mongoClient.getDatabase("test");
		MongoCollection<Document> collection = database.getCollection("foo");
		FindIterable<Document> iter = collection.find();
		for(Document d : iter){
			System.out.println(d.getObjectId("_id"));
			System.out.println(d.toJson());
		}
		//collection.insertOne(new Document().append("name", "sky").append("gender", "man"));
		mongoClient.close();
	}

}
