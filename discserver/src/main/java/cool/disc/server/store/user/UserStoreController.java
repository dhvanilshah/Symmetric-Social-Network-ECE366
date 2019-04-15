package cool.disc.server.store.user;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.*;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import cool.disc.server.model.User;
import cool.disc.server.model.UserBuilder;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class UserStoreController implements UserStore {
    private final Config config;

    // localhost
//    private static MongoClientURI uri = new MongoClientURI("mongodb://localhost:27017/?retryWrites=true");

    MongoClientURI uri;
    private MongoClient dbClient;
    private MongoDatabase database;
    private MongoCollection<Document> userCollection;

    public UserStoreController() {
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
    public User addUser(final String username, final String name, final String password,
                        final String service, final String photo){
        userCollection = database.getCollection(this.config.getString("mongo.collection_user"));

        Document newUser = new Document("id", new ObjectId())
                .append("name", name)
                .append("username", username)
                .append("password", password)
                .append("service", service)
                .append("photo", photo);
        userCollection.insertOne(newUser);

        // show added user information
        ObjectId id = (ObjectId)newUser.get( "_id" );
             return new UserBuilder()
                     .id(id.toHexString())
                     .username(username)
                     .name(name)
                     .password(password)
                     .service(service)
                     .photo(photo)
                     .build();
    }

    // get a list of Users from matching [regex] 'name'
    @Override
    public List<User> getUser(final String name){
        userCollection = database.getCollection(this.config.getString("mongo.collection_user"));

        System.out.println("hello - query");
        Document regQuery = new Document();
        regQuery.append("$regex", "^(?)" + Pattern.quote(name));
        regQuery.append("$options", "i");

        Document findQuery = new Document();
        findQuery.append("name", regQuery);
        FindIterable<Document> iterable = userCollection.find(findQuery);

        List<User> usrlist = new ArrayList<User>();
        for(Document userDoc : iterable){
            ObjectId id = (ObjectId)userDoc.get( "_id" );
            String nameusr = userDoc.getString("name");
            String username = userDoc.getString("username");
            User user = new UserBuilder()
                    .id(id.toHexString())
                    .username(username)
                    .name(nameusr)
                    .build();
            usrlist.add(user);
        }
        return usrlist;
    }

    // get only '_id' from User's 'name'
    @Override
    public ObjectId getUserId(String name) {
        userCollection = database.getCollection(this.config.getString("mongo.collection_user"));

        MongoCursor<Document> foundDoc = null;
        try {
            foundDoc = userCollection.find(new Document("name", name)).iterator();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        ObjectId userId = null;
        if (!foundDoc.hasNext()) {
            return userId; // null
        }
        userId = foundDoc.next().getObjectId("_id");
        return userId;
    }
}
