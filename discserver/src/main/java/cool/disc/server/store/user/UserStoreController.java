package cool.disc.server.store.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.spotify.apollo.Response;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import cool.disc.server.model.Friend;
import cool.disc.server.model.Request;
import cool.disc.server.model.User;
import cool.disc.server.model.UserBuilder;
import okio.ByteString;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.eq;

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
        userCollection = database.getCollection(this.config.getString("mongo.collection_user"));
    }

    public Response<Object> addUser(User newUser){
        // parse data from the payload
        ObjectId newId = new ObjectId();
        String name = newUser.name();
        String password = newUser.password();
        String email = newUser.email();
        String service = newUser.service();
        String photo = newUser.photo();
        Date date = newUser.dateCreated();
        List<Friend> friends = newUser.friends();
        List<Request> reqSent = newUser.reqSent();
        List<Request> reqReceived = newUser.reqReceived();
        List<String> likedPosts = newUser.likedPosts();
        List<String> likedComments = newUser.likedComments();

        Document addUserDoc = new Document("id", newId)
                .append("name", name)
                .append("password", password)
                .append("email", email)
                .append("service", service)
                .append("photo", photo)
                .append("date", date)
                .append("friends", friends)
                .append("reqSent", reqSent)
                .append("reqReceived", reqReceived)
                .append("likedPosts", likedPosts)
                .append("likedComments", likedComments);
        return getObjectResponse(addUserDoc, userCollection);
    }

    // get response from inserting
    public static Response<Object> getObjectResponse(Document addUserDoc, MongoCollection<Document> userCollection) {
        try {
            userCollection.insertOne(addUserDoc);
            return Response.ok();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // get a list of Users from matching [regex] 'name'
    @Override
    public List<User> getUser(final String name){
        Document regQuery = new Document();
        regQuery.append("$regex", "^(?)" + Pattern.quote(name));
        regQuery.append("$options", "i");

        Document findQuery = new Document();
        findQuery.append("name", regQuery);
        FindIterable<Document> iterable = userCollection.find(findQuery);

        List<User> userlist = new ArrayList<User>();
        for(Document userDoc : iterable){
            ObjectId id = (ObjectId)userDoc.get( "_id" );
            String nameUser = userDoc.getString("name");
            String usernameUser = userDoc.getString("username");
            User user = new UserBuilder()
                    .id(id.toHexString())
                    .name(nameUser)
                    .username(usernameUser)
                    .build();
            userlist.add(user);
        }
        return userlist;
    }

    // get only '_id' from User's 'name'
    @Override
    public ObjectId getUserId(String name) {
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

//    take in username and password and return JWT if password is correct
    @Override
    public String login(String username, String password){
        String secret = this.config.getString("secrets.jwt-key");

        Document doc;
        String token = null;

        try {
            doc  = userCollection.find(eq("username", username)).first();
            String pwd = doc.getString("password");
            if(pwd.equals(password)){
                String uid = doc.getObjectId("_id").toHexString();
                try {
                    Algorithm algorithm = Algorithm.HMAC256(secret);
                    token = JWT.create()
                            .withIssuer("auth0")
                            .withClaim("id", uid)
                            .sign(algorithm);
                } catch (JWTCreationException exception){
                    //Invalid Signing configuration / Couldn't convert Claims.
                }
            }
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        if (token != null){
            return token;
//            return Response.ok().withPayload(token);
        } else {
            return "invalid";
//            return Response.of(Status.UNAUTHORIZED, "Password or Username incorrect");
        }
    }
}
