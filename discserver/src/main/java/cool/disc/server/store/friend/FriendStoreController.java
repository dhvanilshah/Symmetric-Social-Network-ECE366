package cool.disc.server.store.friend;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import cool.disc.server.model.Friend;
import org.bson.Document;
import org.bson.types.ObjectId;

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
        String uriString = uri1 + username + password + host;

        // initialize db driver
        uri = new MongoClientURI(uriString);
        dbClient = new MongoClient(uri);
        String databaseString = this.config.getString("mongo.database");
        database = dbClient.getDatabase(databaseString);
    }

    @Override
    public ObjectId getUserId() {

        return null;
    }

    @Override
    public ObjectId getId() {
        return null;
    }

    @Override
    public Integer getScore() {
        return null;
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
