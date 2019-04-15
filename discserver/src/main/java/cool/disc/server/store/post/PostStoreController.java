package cool.disc.server.store.post;

import com.mongodb.MongoClient;
//import com.mongodb.MongoClientException;
import com.mongodb.MongoClientURI;
import com.mongodb.client.*;
//import com.sun.org.apache.xerces.internal.xs.datatypes.ObjectList;
import com.typesafe.config.Config;
import cool.disc.server.model.*;
//import cool.disc.server.store.user.UserStore;
import org.bson.Document;
import org.bson.types.ObjectId;

//import javax.swing.text.html.HTMLDocument;
import java.util.*;

//import static com.spotify.metrics.core.MetricId.build;

public class PostStoreController implements PostStore {
    private final Config config;

    // localhost
//    private static MongoClientURI uri = new MongoClientURI("mongodb://localhost:27017/?retryWrites=true");
//     atlas cloud
    private static MongoClientURI uri = new MongoClientURI("mongodb+srv://admin:puiGLHcv0PKhyLPV@disc-db-shard-00-01-xi3cp.mongodb.net/?retryWrites=true&wtimeoutMS=0");
//    switch between sharded clusters, if it doesn't work (primary may switch automatically)
//        admin:puiGLHcv0PKhyLPV@disc-db-shard-00-01-xi3cp.mongodb.net/?retryWrites=true&wtimeoutMS=0,
//        admin:puiGLHcv0PKhyLPV@disc-db-shard-00-02-xi3cp.mongodb.net/?retryWrites=true&wtimeoutMS=0
    private MongoClient dbClient = new MongoClient(uri);
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
    public Post addPost(ObjectId writerId, ObjectId receiverId, String message) {
        ObjectId newId = new ObjectId();
        Integer privacy = 0;
        Integer likes = 0;
        ObjectId songId = null;
        List<String> comments = null;
        Document addPostDoc = new Document("_id", newId)
                .append("writerId", writerId)
                .append("receiverId", receiverId)
                .append("message", message)
                .append("privacy", privacy)
                .append("likes", likes)
                .append("songId",songId)
                .append("comments", comments);

        try {
            postCollection.insertOne(addPostDoc);

            return new PostBuilder()
                    .id(newId)
                    .writerId(writerId)
                    .receiverId(receiverId)
                    .message(message)
                    .privacy(privacy)
                    .likes(likes)
                    .songId(songId)
                    .comments(comments)
                    .build();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        return null;
    }

    // Utility function for getPosts() below
    // retrieves all friends of the specified user
    public List<Friend> getFriends(String name) {
        List<Friend> friendList = new ArrayList<>();
        try{
            Document queryFriends = new Document("name", name);
            Document inputUser = userCollection.find(queryFriends).iterator().next();
//            User user = new UserBuilder()
//                    .id(inputUser.toString())
//                    .name(inputUser.getString("name"))
//                    .username(inputUser.getString("username"))
//                    .build();
            friendList = (List<Friend>) inputUser.get("friends")    ;
            return friendList;
        } catch (Exception e) {
          System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return friendList;
    }
  // getPosts(userId) --> retrieves all posts written by the specified userId
    public List<Post> getPosts(ObjectId userId) {
        List<Post> postList = new ArrayList<>();
        try {
            Document queryId = new Document("writerId", userId);
            MongoCursor<Document> matchingPosts = postCollection.find(queryId).iterator();
            while (matchingPosts.hasNext()) {
                Document postDoc = matchingPosts.next();
                Post post = new PostBuilder()
                                .id(postDoc.getObjectId("_id"))
                                .writerId(postDoc.getObjectId("writerId"))
                                .receiverId(postDoc.getObjectId("receiverId"))
                                .message(postDoc.getString("message"))
                                .privacy(postDoc.getInteger("privacy"))
                                .likes(postDoc.getInteger("likes"))
                                .songId(postDoc.getObjectId("songId"))
                                .comments((List<String>) postDoc.get("comments"))
                                .build();
//                System.out.println("id: "+postDoc.getObjectId("_id").toString() + " writerId: " + postDoc.getObjectId("writerId").toString());
//                System.out.println("message: "+postDoc.getString("message") + " comments: " + (List<String>) postDoc.get("comments"));
                postList.add(post);
            }
//            System.out.println("postList writer: " + postList.iterator().next().writerId() + " message: " + postList.iterator().next().message());
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        return postList;
    }

    // getFeed(name) - retrieves all posts written by the user and the user's friends
    public List<Post> getFeed(String name) {
        List<Post> postList = new ArrayList<>();
        List<Post> userPosts = new ArrayList<>();
        List<Friend> friendList = new ArrayList<>();
        ObjectId userId = userCollection.find(new Document("name", name)).iterator().next().getObjectId("_id");
        try {
            friendList = getFriends(name);

            if (friendList != null) {
                // iterate through each Friend of the User
                while (friendList.iterator().hasNext()) {
                    ObjectId friendUserId = friendList.iterator().next().userId();
                    List<Post> friendPosts = getPosts(friendUserId);

                    // iterate through selected Friend's posts
                    while (friendPosts.iterator().hasNext()) {
                        Post friendPost = friendPosts.iterator().next();
                        System.out.println("got a friend. post message: " + friendPost.message());
                        postList.add(
                                new PostBuilder()
                                        .id(friendPost.id())
                                        .writerId(friendPost.writerId())
                                        .receiverId(friendPost.receiverId())
                                        .message(friendPost.message())
                                        .build()
                        );
                    }
                }
            }
            userPosts = getPosts(userId);
            int size = userPosts.size();
            Iterator<Post> userPostIterator = userPosts.iterator();
            for (int i = 0; i < size; i++) {
                Post userPost = userPostIterator.next();
//                System.out.println("got the user. post message: " + userPost.message());
                postList.add(
                        new PostBuilder()
                                .id(userPost.id())
                                .writerId(userPost.writerId())
                                .receiverId(userPost.receiverId())
                                .message(userPost.message())
                                .build()
                );
            }
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        System.out.println("postList writer: " + postList.iterator().next().writerId() + " message: " + postList.iterator().next().message());
        return postList;
    }

    // for testing purposes only -- get 'all' posts from the collection
    public List<Post> getAllPosts() {
        MongoCollection<Document> postCollection = database.getCollection("post");

        MongoCursor<Document> cursor = postCollection.find().iterator();
        List<Post> postEntries = new ArrayList<Post>();
        try{
            while(cursor.hasNext()) {
                Document nextentry = cursor.next();
                Post post = new PostBuilder()
                        .id(nextentry.getObjectId("_id"))
                        .writerId(nextentry.getObjectId("writerId"))
                        .receiverId(nextentry.getObjectId("receiverId"))
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
