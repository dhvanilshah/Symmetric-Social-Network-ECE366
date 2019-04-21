package cool.disc.server.store.user;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientException;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import cool.disc.server.model.User;
import cool.disc.server.model.UserBuilder;
import cool.disc.server.utils.AuthUtils;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.eq;

public class UserStoreController implements UserStore {
    private final Config config;
    private final AuthUtils authUtils;

    // localhost
//    private static MongoClientURI uri = new MongoClientURI("mongodb://localhost:27017/?retryWrites=true");

    MongoClientURI uri;
    private MongoClient dbClient;
    private MongoDatabase database;
    private MongoCollection<Document> userCollection;

    public UserStoreController() {
        this.authUtils =  new AuthUtils();
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
    public Integer addUser(User newUser){
        // parse data from the payload
        String name = newUser.name();
        String username = newUser.username();
        String password = newUser.password();
        String email = newUser.email();
        String service = newUser.service();
//        String photo = newUser.photo();
        Date date = new Date();

        Document addUserDoc = new Document()
                .append("name", name)
                .append("username", username)
                .append("password", password)
                .append("email", email)
                .append("service", service)
//                .append("photo", photo)
                .append("date", date);

        try {
            userCollection.insertOne(addUserDoc);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
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
        Document doc;
        String token = null;

        try {
            doc  = userCollection.find(eq("username", username)).first();
            String pwd = doc.getString("password");
            if(pwd.equals(password)){
                String uid = doc.getObjectId("_id").toHexString();
                token = authUtils.createToken(uid);
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

    @Override
    public String addFriend(String friend_id, String user_id){
        Document requestPending;
        Document requestSent;

        requestPending = new Document("userId", new ObjectId(user_id)).append("date", new Date());
        requestSent = new Document("userId",  new ObjectId(friend_id)).append("date", new Date());

        try {
            userCollection.updateOne(eq("_id",  new ObjectId(user_id)), Updates.push("reqSent", requestSent));
            userCollection.updateOne(eq("_id",  new ObjectId(friend_id)), Updates.push("reqReceived", requestPending));
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }

        return "okay";
    }

    @Override
    public String handleRequest(String friend_id, String user_id, String action){
        Document friend;
        Document user;
        List<Document> requests;
//        Step 1: get user, friend
//        Step 2: delete request sent, delete request received
//        Step 3: add user to friend's list and add friend to user's list

        try {
            user = userCollection.find(eq("_id", new ObjectId(user_id))).first();
            friend = userCollection.find(eq("_id", new ObjectId(friend_id))).first();

            ArrayList<Document> reqRec =  (ArrayList<Document>) user.get("reqReceived");
            ArrayList<Document> reqSent =  (ArrayList<Document>) friend.get("reqSent");

            for(int i = 0; i < reqRec.size(); i++){
                Document doc = reqRec.get(i);
                if(doc.get("userId").equals(new ObjectId(friend_id))){
                    reqRec.remove(doc);
                }
            }

            for(int i = 0; i < reqSent.size(); i++){
                Document doc = reqSent.get(i);
                if(doc.get("userId").equals(new ObjectId(user_id))){
                    reqSent.remove(doc);
                }
            }

            BasicDBObject userReqRec = new BasicDBObject("$set", new BasicDBObject("reqReceived", reqRec));
            userCollection.updateOne(eq("_id",  new ObjectId(user_id)), userReqRec);

            BasicDBObject friendReqSent = new BasicDBObject("$set", new BasicDBObject("reqSent", reqSent));
            userCollection.updateOne(eq("_id",  new ObjectId(friend_id)), friendReqSent);

            if(action.equals("add")){
                user = new Document("userId", new ObjectId(user_id)).append("score", 0);
                friend = new Document("userId", new ObjectId(friend_id)).append("score", 0);
                userCollection.updateOne(eq("_id",  new ObjectId(user_id)), Updates.push("friends", friend));
                userCollection.updateOne(eq("_id",  new ObjectId(friend_id)), Updates.push("friends", user));
            }

        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }

        return "okay";
    }


}