package cool.disc.server.store.friend;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import cool.disc.server.model.Friend;
import org.bson.Document;

import java.util.List;

public class FriendStoreController implements FriendStore {
    private final Config config;

    MongoClientURI uri;
    private MongoClient dbClient;
    private MongoDatabase database;
    private MongoCollection<Document> postCollection;
    private MongoCollection<Document> userCollection;

    public FriendStoreController() {
        this.config = ConfigFactory.load("discserver.conf");


        // get login info from config
        String uri1 = this.config.getString("mongo.uri");
        String username = this.config.getString("mongo.username");
        String password = this.config.getString("mongo.password");
        String host = this.config.getString("mongo.host");
        String host2 = this.config.getString("mongo.host2");
        String host3 = this.config.getString("mongo.host3");
        String uriString = uri1 + username + password;

        // initialize db driver
        uri = new MongoClientURI(uriString+host);
        try {
            dbClient = new com.mongodb.MongoClient(uri);
        } catch (MongoClientException e) {
            try {
                uri = new MongoClientURI(uriString+host2);
                dbClient = new com.mongodb.MongoClient(uri);
            } catch (Exception error) {
                uri = new MongoClientURI(uriString+host3);
                dbClient = new com.mongodb.MongoClient(uri);

            }
        }
        String databaseString = this.config.getString("mongo.database");
        database = dbClient.getDatabase(databaseString);
        userCollection = database.getCollection(this.config.getString("mongo.collection_user"));

    }


    @Override
    public List<Friend> getFriendList() {
        return null;
    }

    @Override
    public Friend addFriend() {
        return null;
    }

    @Override
    public Friend removeFriend() {
        return null;
    }
}
