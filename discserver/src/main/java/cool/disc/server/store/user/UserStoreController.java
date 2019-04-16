package cool.disc.server.store.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.mongodb.DBObject;
import com.mongodb.MongoClientURI;
import com.mongodb.client.*;
import com.spotify.apollo.Response;
import com.spotify.apollo.Status;
import com.typesafe.config.Config;
import cool.disc.server.model.User;
import cool.disc.server.model.UserBuilder;
import okio.ByteString;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.eq;

//CHANGE ADDUSER TO POST AND GET JSON DATA FROM THE POST
//WRITE LOGIN POST METHOD
public class UserStoreController implements UserStore {

//    private MongoClient mongoClient = MongoClients.create();
    private static MongoClientURI uri = new MongoClientURI("mongodb+srv://admin:puiGLHcv0PKhyLPV@disc-db-shard-00-00-xi3cp.mongodb.net/?retryWrites=true&wtimeoutMS=0,admin:puiGLHcv0PKhyLPV@disc-db-shard-00-01-xi3cp.mongodb.net/?retryWrites=true&wtimeoutMS=0,admin:puiGLHcv0PKhyLPV@disc-db-shard-00-02-xi3cp.mongodb.net/?retryWrites=true&wtimeoutMS=0");
    private com.mongodb.MongoClient dbClient = new com.mongodb.MongoClient(uri);
    private MongoDatabase database = dbClient.getDatabase("main-db");
    private MongoCollection<Document> userCollection = database.getCollection("user");

    private final Config config;

    public UserStoreController(final Config config) {
        this.config = config;
    }

    @Override
    public User addUser(final String username,
                        final String name,
                        final String password,
                        final String service,
                        final String photo){

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
        MongoCursor<Document> foundDoc = null;
        ObjectId userId = null;
        try {
            foundDoc = userCollection.find(new Document("name", name)).iterator();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        if (foundDoc.hasNext()) {
            userId = foundDoc.next().getObjectId("_id");
        }
        return userId;
    }

//    take in username and password and return JWT if password is correct
    @Override
    public String login(String username, String password){
        Document doc;
        String token = null;

        try {
            doc  = userCollection.find(eq("username", username)).first();
            String pwd = doc.getString("password");
            if(pwd.equals(password)){
                String uid = doc.getObjectId("_id").toHexString();
                try {
                Algorithm algorithm = Algorithm.HMAC256("andyjeongscrummaster");
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
