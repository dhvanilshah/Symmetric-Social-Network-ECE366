package cool.disc.server.store.post;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.typesafe.config.Config;
import cool.disc.server.model.Post;
import cool.disc.server.model.User;
import cool.disc.server.model.UserBuilder;
import org.bson.Document;
import org.bson.types.ObjectId;
import cool.disc.server.model.PostBuilder;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PostStoreController implements PostStore {
    private final Config config;

    // localhost
    private final static String HOST = "localhost";
    private final static int PORT = 27017;
    private static MongoClient dbclient = new MongoClient(HOST, PORT);

    // atlas cloud
    private static MongoClientURI uri = new MongoClientURI(
            "mongodb+srv://admin:puiGLHcv0PKhyLPV@disc-db-shard-00-00-xi3cp.mongodb.net");
    private static MongoClient dbClient = new MongoClient(uri);
    private MongoDatabase database = dbClient.getDatabase("main-db");
    private MongoCollection<Document> postCollection = database.getCollection("post");
    private MongoCollection<Document> userCollection = database.getCollection("user");

    public PostStoreController(final Config config) {
        this.config = config;
    }

    // (GET) addPost?message
    // in the driver function, receiverId and message would be passed in.
    // writerId is still here until we can get it from the token (passed on from frontend)
    @Override
    public void addPost(String writerId, String receiverId, String message) {
        ObjectId newPostId = new ObjectId();
        Document newPostDocument = new Document("_id", newPostId)
                .append("message", message);
        postCollection.insertOne(newPostDocument);
    }

    // Utility function for getPosts() below
    // retreives all friends of the specified user
    public List<User> getFriends(String id) {
        ObjectId _id = new ObjectId(id);
        Document queryFriends = new Document("_id", _id);
        MongoCursor<Document> friendsCursor = userCollection.find(queryFriends).iterator();

        List<User> friendList = null;
        try {
            while (friendsCursor.hasNext()) {
                Document friendDocument = friendsCursor.next();
                User friend = new UserBuilder()
                        .id(friendDocument.getObjectId("_id").toString())
                        .name(friendDocument.getString("name"))
                        .username(friendDocument.getString("username"))
                        .build();
                friendList.add(friend);
            }
        }
        catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        finally {
            friendsCursor.close();
        }
        return friendList;
    }
    // (GET) getPosts/userId
    // get posts from the user (= userId) and the user's friends
    public List<Post> getPosts(final String userId) {
        List<User> friendList = getFriends(userId);
        Document queryMe = new Document("_id", userId);
        Document myUserDocument= userCollection.find(queryMe).iterator().next();
        User myUser = new UserBuilder()
                .id(myUserDocument.getObjectId("_id").toString())
                .name(myUserDocument.getString("name"))
                .username(myUserDocument.getString("username"))
                .build();
        // add (User) myself to the list of friendList, from each of which posts will be pulled
        friendList.add(myUser);

        List<Post> allPosts = null;
        // query for posts from each friend
        for (User friend : friendList) {
            Document queryPosts = new Document("receiverId", friend.id());
            MongoCursor<Document> cursor = postCollection.find(queryPosts).iterator();
            try{
                while(cursor.hasNext()) {
                    Document nextentry = cursor.next();
                    Post post = new PostBuilder()
                            .id(nextentry.getObjectId("_id").toString())
                            .receiverId(nextentry.getObjectId("receiverId").toString())
                            .writerId(nextentry.getObjectId("writerId").toString())
                            .message(nextentry.getString("message"))
                            .build();
                    allPosts.add(post);
                }
            }
            catch(Exception e){
                System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            }
            finally {
                cursor.close();
            }
        }
        return allPosts;
    }

    //- (GET) getFeed (calls getPosts)
    public List<Post> getFeed() {
        // for now, printing all posts,
        // but later, get the user in session from the token, and
        // get posts from only friends of the user
        List<Post> allPosts = null;
        MongoCursor<Document> userCursor = userCollection.find().iterator();
        try{
            while (userCursor.hasNext()) {
                ObjectId userId = userCursor.next().getObjectId("_id");
                List<Post> friendsPosts = getPosts(userId.toString());
                for (Post post : friendsPosts) {
                    allPosts.add(post);
                }
            }
        }
        catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        finally {
            userCursor.close();
        }
        return allPosts;
    }

    // for testing purposes only -- get 'all' posts from the collection
    public List<Post> getAllPosts() {
        MongoCursor<Document> cursor = postCollection.find().iterator();
        List<Post> postEntries = new ArrayList<Post>();
        try{
            while(cursor.hasNext()) {
                Document nextentry = cursor.next();
                Post post = new PostBuilder()
                        .id(nextentry.getObjectId("_id").toString())
                        .writerId(nextentry.getObjectId("writerId").toString())
                        .receiverId(nextentry.getObjectId("receiverId").toString())
                        .message(nextentry.getString("message"))
                        .build();
                postEntries.add(post);
            }
        }
        catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        finally {
            cursor.close();
        }
        return postEntries;
    }
}
