package backend;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClient;
import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.MongoClientURI;
import java.util.*;

public class MongoConnection{
    private static volatile MongoConnection instance;
    private static MongoClient mongoClient;
    private static int port = 27017;
    private String database2 = "twitter";
    private String collection2 = "tweet";
    private DBCollection collection;
    private DB database;

    private MongoConnection(){}

    public static MongoConnection getMongo(){
        if (instance == null){
            synchronized (MongoConnection.class){
                if (instance == null){
                    instance = new MongoConnection();
                }
            }
        }
        return instance;
    }

    public void OpenMongoClient(){
        if (mongoClient == null)
            mongoClient = new MongoClient("18.220.76.177",port);
    }

    public MongoCollection<Document> getCollection(){

        MongoDatabase mongoDatabase = mongoClient.getDatabase(database2);
        return mongoDatabase.getCollection(collection2);
    }

    public void closeMongoClient(){
        mongoClient.close();
    }

    public DBCursor getTweets(){
        DB mongoDatabase = mongoClient.getDB(database2);
        collection = mongoDatabase.getCollection(collection2);
        System.out.println(collection);
        DBCursor cursor = collection.find();
        return cursor;
    }
}