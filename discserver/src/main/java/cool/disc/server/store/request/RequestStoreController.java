package cool.disc.server.store.request;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;

public class RequestStoreController implements RequestStore {
    private final Config config;

    MongoClientURI uri;
    private MongoClient dbClient;
    private MongoDatabase database;
    private MongoCollection<Document> postCollection;
    private MongoCollection<Document> userCollection;

    public RequestStoreController() {
        this.config = ConfigFactory.load("discserver.conf");

        // get login info from config
        String uri1 = this.config.getString("mongo.uri");
        String username = this.config.getString("mongo.username");
        String password = this.config.getString("mongo.password");
        String host = this.config.getString("mongo.host");
        String host2 = this.config.getString("mongo.host2");
        String host3 = this.config.getString("mongo.host3");
        String uriString = uri1 + username + password;

//         initialize db driver
        uri = new MongoClientURI(uri1);
        dbClient = new com.mongodb.MongoClient(uri);
        String databaseString = this.config.getString("mongo.database");
        database = dbClient.getDatabase(databaseString);

        // database
        String userdb = this.config.getString("mongo.collection_user");
        String postdb = this.config.getString("mongo.collection_post");
        String songdb = this.config.getString("mongo.collection_song");
        userCollection = database.getCollection(userdb);
        postCollection = database.getCollection(postdb);
    }

    @Override
    public ObjectId getUserId() {
        return null;
    }

    @Override
    public Date getDate() {
        return null;
    }
}
