package dao.mongoInterface;

import java.util.ArrayList;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public interface MongoInterface {  
	    /** 
	     *  
	     * @method£ºgetDb  
	     * @author£ºRui 
	     * @param dbName 
	     * @return 
	     * description£ºget database by DB's name 
	     */  
	    public DB getDb(String dbName);  
	    /** 
	     *  
	     * @method£ºgetCollection  
	     * @author£ºRui 
	     * @param dbName    
	     * @param collectionName     
	     * @return 
	     * description£ºget collection by DB name & collection name
	     */  
	    public DBCollection getCollection(String dbName, String collectionName);  
	    /** 
	     *  
	     * @method£ºinSert 
	     * @author£ºRui 
	     * @param dbName 
	     * @param collectionName 
	     * @param keys 
	     * @param values 
	     * @return 
	     * description: add keys and value based on DB name
	     */  
	    public boolean inSert(String dbName, String collectionName, String[] keys, Object[] values);  
	    /** 
	     *  
	     * @method£ºdelete 
	     * @author£ºRui 
	     * @param dbName 
	     * @param collectionName 
	     * @param keys 
	     * @param values 
	     * @return 
	     * description: delete specific values and keys based on DB name and values
	     */  
	    public boolean delete(String dbName, String collectionName, String[] keys, Object[] values);  
	    /** 
	     *  
	     * method£ºfind 
	     * author£ºRui 
	     * @param dbName 
	     * @param collectionName 
	     * @param keys 
	     * @param values 
	     * @param num 
	     * @return
	     * description£º find DBObject based on DB name, Collection name , Keys , and Values, num.
	     */  
	    public ArrayList<DBObject> find(String dbName, String collectionName, String[] keys, Object[] values, int num);  
	    /** 
	     *  
	     * method£ºupdate 
	     * author£ºRui 
	     * @param dbName 
	     * @param collectionName 
	     * @param oldValue 
	     * @param newValue 
	     * @return
	     * Description: update DB with new value 
	     */  
	    public boolean update(String dbName, String collectionName, DBObject oldValue, DBObject newValue);  
	    /** 
	     *  
	     * @method£ºisExit 
	     * @author: Rui 
	     * @param dbName 
	     * @param collectionName 
	     * @param keys 
	     * @param values 
	     * @return
	     * description: keys or values is exit in CollectionName(DB)  
	     */  
	    public boolean isExit(String dbName, String collectionName, String key, Object value);
}
	      
	

