package cool.disc.server.store.comment;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import cool.disc.server.model.Comment;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;

public class CommentStoreController implements CommentStore {
    private final Config config;

    MongoClientURI uri;
    private MongoClient dbClient;
    private MongoDatabase database;
    private MongoCollection<Document> postCollection;
    private MongoCollection<Document> userCollection;

    public CommentStoreController() {
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
    public Comment addComment() {
        return null;
    }

    @Override
    public Comment deleteComment() {
        return null;
    }

    @Override
    public ObjectId getId() {
        return null;
    }

    @Override
    public ObjectId getWriterId() {
        return null;
    }

    @Override
    public ObjectId getPostId() {
        return null;
    }

    @Override
    public Date getDate() {
        return null;
    }

    @Override
    public Integer getLikes() {
        return null;
    }
}
