package dao;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class DataAccessObject {
	private MongoClient mongo =null;
	private DB db = null;
	private DBCollection dbCollection = null;
	
	public void init(){
		try{
			mongo = new MongoClient("localhost",27017);
			db = mongo.getDB("rlih");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public DBCursor getNews(){
		init();
		dbCollection = db.getCollection("news");
		DBCursor cursor = dbCollection.find();
        return cursor;
	}
	public void destory(){
		if(mongo!=null){
			mongo.close();
			mongo = null;
			db = null;
		}
	}
	public static void main(String[] args) throws UnknownHostException{
		MongoClient mongo = new MongoClient("localhost", 27017);
		List<String> dbs = mongo.getDatabaseNames();
		System.out.println(dbs);
		
		DB db = mongo.getDB("rlih");
        
		Set<String> collections = db.getCollectionNames();
		System.out.println(collections); // [datas, names, system.indexes, users]
		
		DBCollection col = db.getCollection("news");
		DBCursor cursor = col.find();
        while(cursor.hasNext()){
            System.out.println(cursor.next());
        }
		System.out.println();
	}
}
