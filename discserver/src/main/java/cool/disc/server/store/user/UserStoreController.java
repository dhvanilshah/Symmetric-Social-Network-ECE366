package cool.disc.server.store.user;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import cool.disc.server.model.User;
import cool.disc.server.model.UserBuilder;
import cool.disc.server.utils.AuthUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.eq;

public class UserStoreController implements UserStore {
    private final Config config;
    private final AuthUtils authUtils;
    private final int logRounds;

    // localhost
//    private static MongoClientURI uri = new MongoClientURI("mongodb://localhost:27017/?retryWrites=true");

    MongoClientURI uri;
    private MongoClient dbClient;
    private MongoDatabase database;
    private MongoCollection<Document> userCollection;
    private MongoCollection<Document> testCollection;
    private Object search;

    public UserStoreController() {
        this.logRounds = 13;
        this.authUtils =  new AuthUtils();
        this.config = ConfigFactory.load("discserver.conf");
        // get login info from config
//        String username = this.config.getString("mongo.username");
//        String password = this.config.getString("mongo.password");
//         initialize db driver

//      localhost for testing
//        MongoClient dbClient = new MongoClient( "localhost" , 27017 );
//        database = dbClient.getDatabase("discbase");
//        String postdb = this.config.getString("mongo.collection_post");
//        String songdb = this.config.getString("mongo.collection_song");

        // database
        String uri = this.config.getString("mongo.uri");
        dbClient = new com.mongodb.MongoClient(new MongoClientURI(uri));
        String databaseString = this.config.getString("mongo.database");
        database = dbClient.getDatabase(databaseString);
        String userdb = this.config.getString("mongo.collection_user");
        userCollection = database.getCollection(userdb);
    }

    @Override
    public Integer addUser(User newUser){
        // parse data from the payload
        String name = newUser.name();
        String username = newUser.username();
        String password = newUser.password();
        String email = newUser.email();
        String birthday = newUser.birthday();
        String hashword = hash(password);
//        String photo = newUser.photo();
        Date date = new Date();

        Document addUserDoc = new Document()
                .append("name", name)
                .append("username", username)
                .append("password", hashword)
                .append("email", email)
                .append("birthday", birthday)
//                .append("photo", photo)
                .append("date", date);

        Document doc  = userCollection.find(eq("username", username)).first();
        if(doc != null){
            return 2;
        }

        Document doc2  = userCollection.find(eq("email", email)).first();
        if(doc2 != null){
            return 3;
        }

        try {
            userCollection.insertOne(addUserDoc);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // get a list of Users from matching [regex] 'name'
    @SuppressWarnings("Duplicates")
    @Override
    public List<User> getUser(final String name, String user_id){
        Document doc;

        try {
            doc  = userCollection.find(eq("_id", new ObjectId(user_id))).first();
            ArrayList<Document> friends =  (ArrayList<Document>) doc.get("friends");
            String username = doc.getString("username");


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

                if(usernameUser.equals(username)){
                    continue;
                } else if(checkFriend(id, friends)){
                    User user = new UserBuilder()
                            .id(id.toHexString())
                            .name(nameUser)
                            .username(usernameUser)
                            .friendCheck(true)
                            .build();
                    userlist.add(user);
                } else {
                    User user = new UserBuilder()
                            .id(id.toHexString())
                            .name(nameUser)
                            .username(usernameUser)
                            .friendCheck(false)
                            .build();
                    userlist.add(user);
                }

            }
            return userlist;
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        return null;
    }

    private Boolean checkFriend(ObjectId id, ArrayList<Document> friendsList){
        if(friendsList != null) {
            for (Document friend : friendsList) {
                if (friend.getObjectId("userId").equals(id)) {
                    return true;
                }
            }
        }
        return false;
    }

    //    take in username and password and return JWT if password is correct
    @Override
    public String login(String username, String password){
        Document doc;
        String token = null;

        try {
            doc  = userCollection.find(eq("username", username)).first();
            String pwd = doc.getString("password");
            if(verifyHash(password, pwd)){
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


    private String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(logRounds));
    }

    private boolean verifyHash(String password, String hash) {
        return BCrypt.checkpw(password, hash);
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

    @SuppressWarnings("Duplicates")
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

            if(action.equals("accept")){
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


    @SuppressWarnings("Duplicates")
    @Override
    public List<User> getRequests(String user_id){
        Document user;
        List<User> userList = new ArrayList<User>();
        ArrayList<Document> reqRec;
        try {
            user = userCollection.find(eq("_id", new ObjectId(user_id))).first();
            reqRec =  (ArrayList<Document>) user.get("reqReceived");
            for(int i = 0; i < reqRec.size(); i++) {
                Document doc = reqRec.get(i);
                Document requestSender;
                try {
                    requestSender = userCollection.find(eq("_id", doc.get("userId"))).first();
                    ObjectId id = (ObjectId) requestSender.get("_id");
                    String nameUser = requestSender.getString("name");
                    String usernameUser = requestSender.getString("username");
                    User reqSender = new UserBuilder()
                            .id(id.toHexString())
                            .name(nameUser)
                            .username(usernameUser)
                            .build();
                    userList.add(reqSender);
                } catch (Exception e) {
                    System.err.println(e.getClass().getName() + ": " + e.getMessage());
                }
            }
        }
        catch (Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }

        return userList;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public List<User> getFriends(String user_id){
        Document user;
        List<User> userList = new ArrayList<User>();
        ArrayList<Document> reqRec;
        try {
            user = userCollection.find(eq("_id", new ObjectId(user_id))).first();
            reqRec =  (ArrayList<Document>) user.get("friends");
            for(int i = 0; i < reqRec.size(); i++) {
                Document doc = reqRec.get(i);
                Document requestSender;
                try {
                    requestSender = userCollection.find(eq("_id", doc.get("userId"))).first();
                    ObjectId id = (ObjectId) requestSender.get("_id");
                    String nameUser = requestSender.getString("name");
                    String usernameUser = requestSender.getString("username");
                    User reqSender = new UserBuilder()
                            .id(id.toHexString())
                            .name(nameUser)
                            .username(usernameUser)
                            .build();
                    userList.add(reqSender);
                } catch (Exception e) {
                    System.err.println(e.getClass().getName() + ": " + e.getMessage());
                }
            }
        }
        catch (Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }

        return userList;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public User getBio(String user_id, String username) {
        Document userDoc;
        User user;
        userDoc = userCollection.find(eq("_id", new ObjectId(user_id))).first();
        if(username.equals(userDoc.getString("username"))){
            try {
                user = new UserBuilder()
                        .id((userDoc.getObjectId("_id")).toHexString())
                        .name(userDoc.getString("name"))
                        .username(userDoc.getString("username"))
                        .birthday(userDoc.getString("birthday"))
                        .bio(userDoc.getString("bio"))
                        .faveSong(userDoc.getString("faveSong"))
                        .name(userDoc.getString("name"))
                        .username(userDoc.getString("username"))
                        .friendCheck(true)
                        .build();
                return user;
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }
        else {
            try {
                userDoc = userCollection.find(eq("username", username)).first();
                ArrayList<Document> friends =  (ArrayList<Document>) userDoc.get("friends");
                Boolean friendCheck = checkFriend(new ObjectId(user_id), friends);
                user = new UserBuilder()
                        .id((userDoc.getObjectId("_id")).toHexString())
                        .name(userDoc.getString("name"))
                        .username(userDoc.getString("username"))
                        .birthday(userDoc.getString("birthday"))
                        .bio(userDoc.getString("bio"))
                        .faveSong(userDoc.getString("faveSong"))
                        .name(userDoc.getString("name"))
                        .username(userDoc.getString("username"))
                        .friendCheck(friendCheck)
                        .build();
                return user;
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }
        return null;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public Integer updateBio(String user_id, User user) {
        String bio = user.bio();
        String birthday = user.birthday();
        String faveSong= user.faveSong();

        try{
            BasicDBObject updates = new BasicDBObject();
            updates.append("$set", new BasicDBObject()
                    .append("bio", bio)
                    .append("birthday", birthday)
                    .append("faveSong", faveSong));
            userCollection.updateOne(eq("_id",  new ObjectId(user_id)), updates);
            return 0;

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    return 1;
    }

}
