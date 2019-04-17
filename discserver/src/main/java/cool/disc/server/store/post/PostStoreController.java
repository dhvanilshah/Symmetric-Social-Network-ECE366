package cool.disc.server.store.post;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.spotify.apollo.Response;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import cool.disc.server.model.Friend;
import cool.disc.server.model.Post;
import cool.disc.server.model.PostBuilder;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PostStoreController implements PostStore {
    private final Config config;

    // localhost
//    private static MongoClientURI uri = new MongoClientURI("mongodb://localhost:27017/?retryWrites=true");

    MongoClientURI uri;
    private MongoClient dbClient;
    private MongoDatabase database;
    private MongoCollection<Document> postCollection;
    private MongoCollection<Document> userCollection;

    public PostStoreController() {
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

    // (GET) addPost?message
    // in the driver function, receiverId and message would be passed in.
    // writerId is still here until we can get it from the token (passed on from frontend)
    @Override
    public Response<Object> addPost(Post newPost) {
        postCollection = database.getCollection(this.config.getString("mongo.collection_post"));

        // parse data from the payload
        ObjectId newId = new ObjectId();
        ObjectId writerId = newPost.writerId();
        ObjectId receiverId = newPost.receiverId();
        Integer privacy = newPost.privacy();
        String message = newPost.message().toString();
        Integer likes = newPost.likes();
        ObjectId songId = newPost.songId();
        List<String> comments = newPost.comments();
        // create document to insert
        Document addPostDoc = new Document("_id", newId)
                .append("writerId", writerId)
                .append("receiverId", receiverId)
                .append("privacy", privacy)
                .append("message", message)
                .append("likes", likes)
                .append("songId",songId)
                .append("comments", comments);
        return getObjectResponse(addPostDoc, postCollection);
    }

    // get response from inserting
    private Response<Object> getObjectResponse(Document addPostDoc, MongoCollection<Document> postCollection) {
        try {
            userCollection.insertOne(addPostDoc);
            return Response.ok();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        return null;
    }

    // Utility function for getPosts() below
    // retrieves all friends of the specified user
    public List<Friend> getFriends(String name) {

        postCollection = database.getCollection(this.config.getString("mongo.collection_post"));
        userCollection = database.getCollection(this.config.getString("mongo.collection_user"));
        List<Friend> friendList = new ArrayList<>();
        try{
            Document queryFriends = new Document("name", name);
            Document inputUser = userCollection.find(queryFriends).iterator().next();
            friendList = (List<Friend>) inputUser.get("friends")    ;
            return friendList;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return friendList;
    }
    // getPosts(userId) --> retrieves all posts written by the specified userId
    public List<Post> getPosts(ObjectId userId) {
        postCollection = database.getCollection(this.config.getString("mongo.collection_post"));
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

        userCollection = database.getCollection(this.config.getString("mongo.collection_user"));
        List<Post> postList = new ArrayList<>();
        ObjectId userId = userCollection.find(new Document("name", name)).iterator().next().getObjectId("_id");
        try {
            List<Friend> friendList = getFriends(name);

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
            List<Post> userPosts = getPosts(userId);
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
        postCollection = database.getCollection(this.config.getString("mongo.collection_post"));

        MongoCursor<Document> cursor = postCollection.find().iterator();
        List<Post> postEntries = new ArrayList<Post>();
        try{
            while(cursor.hasNext()) {
                Document nextEntry = cursor.next();
                Post post = new PostBuilder()
                        .id(nextEntry.getObjectId("_id"))
                        .writerId(nextEntry.getObjectId("writerId"))
                        .receiverId(nextEntry.getObjectId("receiverId"))
                        .message(nextEntry.getString("message"))
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
