package cool.disc.server.store.song;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.bson.Document;
import org.bson.types.ObjectId;

import javax.annotation.Nullable;

public class SongStoreController implements SongStore {
    private final Config config;

    MongoClientURI uri;
    private MongoClient dbClient;
    private MongoDatabase database;
    private MongoCollection<Document> postCollection;
    private MongoCollection<Document> userCollection;

    public SongStoreController() {
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
    public ObjectId getSongId() {
        return null;
    }

    @Override
    public String artist() {
        return null;
    }

    @Nullable
    @Override
    public String album() {
        return null;
    }

    @Nullable
    @Override
    public Integer score() {
        return null;
    }

    @Override
    public String title() {
        return null;
    }

    @Nullable
    @Override
    public String url() {
        return null;
    }
}
