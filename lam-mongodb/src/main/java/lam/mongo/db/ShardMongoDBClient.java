package lam.mongo.db;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
* <p>
* shard mongodb client
* </p>
* @author linanmiao
* @date 2016年12月5日
* @version 1.0
*/
public class ShardMongoDBClient {
	
	private MongoClient mongoClient;
	
	private static final String DEFAULT_DATABASE_NAME = "test";
	
	public ShardMongoDBClient(){
		List<ServerAddress> seeds = new ArrayList<>();
		ServerAddress seed1 = new ServerAddress("192.168.204.127", 50000);
		ServerAddress seed2 = new ServerAddress("192.168.204.127", 50001);
		ServerAddress seed3 = new ServerAddress("192.168.204.127", 50002);
		seeds.add(seed1);
		seeds.add(seed2);
		seeds.add(seed3);
		mongoClient = new MongoClient(seeds);
		System.out.println("Shard MongoClient init:" + seeds);
	}
	
	public void close(){
		if(mongoClient != null){
			mongoClient.close();
			System.out.println("MongoClient closed.");
		}
	}
	
	private MongoClient getMongoClient(){
		return mongoClient;
	}
	
	public MongoDatabase getDefaultDatabase(){
		return getMongoClient().getDatabase(DEFAULT_DATABASE_NAME);
	}
	
	public <T> MongoCollection<Document> getCollection(Class<T> clazz){
		return getDefaultDatabase().getCollection(clazz.getSimpleName().toLowerCase());
	}

}
