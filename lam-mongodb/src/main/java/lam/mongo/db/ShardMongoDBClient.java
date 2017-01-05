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
		
//MongoClient注释说明，只会使用其中的一台mongos连接。
/**
* Creates a Mongo based on a list of replica set members or a list of mongos. It will find all members (the master will be used by
*  default). If you pass in a single server in the list, the driver will still function as if it is a replica set. 
*  If you have a standalone server, use the Mongo(ServerAddress) constructor.

* If this is a list of mongos servers, it will pick the closest (lowest ping time) one to send all requests to, and automatically
*  fail over to the next server if the closest is down.
*/
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
