package lam.mongo.demo;

import org.bson.Document;

import com.google.gson.Gson;

import lam.mongo.db.ShardMongoDBClient;
import lam.mongo.model.Foo;

/**
* <p>
* demo
* </p>
* @author linanmiao
* @date 2016年12月5日
* @version 1.0
*/
public class ShardDemoTest {
	
	private static Gson gson = new Gson();
	
	public static void main(String[] args){
		ShardMongoDBClient client = new ShardMongoDBClient();
		
		insertFooTest(client);
		
		client.close();
	}
	
	private static void insertFooTest(ShardMongoDBClient client){
		Foo foo = new Foo();
		foo.setName("waoo");
		foo.setFullname("wannoooo");
		
		client.getCollection(Foo.class).insertOne(Document.parse(gson.toJson(foo)));;
	}

}
