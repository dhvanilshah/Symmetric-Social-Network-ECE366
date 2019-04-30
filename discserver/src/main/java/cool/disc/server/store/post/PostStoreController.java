package cool.disc.server.store.post;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.spotify.apollo.Response;
import com.spotify.apollo.Status;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import cool.disc.server.model.Friend;
import cool.disc.server.model.Post;
import cool.disc.server.model.PostBuilder;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PostStoreController implements PostStore {
    private static final Logger LOG = LoggerFactory.getLogger(PostStoreController.class);

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
        String host2 = this.config.getString("mongo.host2");
        String host3 = this.config.getString("mongo.host3");
        String uriString = uri1 + username + password;

//         initialize db driver
        uri = new MongoClientURI(uriString+host);
//        try {
//            dbClient = new com.mongodb.MongoClient(uri);
//        } catch (MongoClientException e) {
//            try {
//                uri = new MongoClientURI(uriString+host2);
//                dbClient = new com.mongodb.MongoClient(uri);
//            } catch (Exception error) {
//                uri = new MongoClientURI(uriString+host3);
//                dbClient = new com.mongodb.MongoClient(uri);
//            }
//        }
        dbClient = new com.mongodb.MongoClient(uri);
        String databaseString = this.config.getString("mongo.database");
        database = dbClient.getDatabase(databaseString);

//      Temporary overwrite for testing
        MongoClient dbClient = new MongoClient( "localhost" , 27017 );
        database = dbClient.getDatabase("discbase");
        postCollection = database.getCollection("posts");
        userCollection = database.getCollection("users");
    }

    // (GET) addPost
    // in the driver function, receiverId and message would be passed in.
    // writerId is still here until we can get it from the token (passed on from frontend)
    @Override
    public Response<Object> addPost(Post newPost, String user_id) {

        // parse data from the payload
        ObjectId newId = new ObjectId();
        ObjectId writerId = new ObjectId(user_id);
        ObjectId receiverId;
        if("self".equals(newPost.receiverIdString())){
            receiverId = writerId;
        } else {
            receiverId = new ObjectId(newPost.receiverIdString());
        }
        Integer privacy = newPost.privacy();
        String message = newPost.message();
        Integer likes = newPost.likes();
        ObjectId songId = new ObjectId(newPost.songIdString());
//        List<String> comments = newPost.comments();
        // create document to insert
        Document addPostDoc = new Document("_id", newId)
                .append("writerId", writerId)
                .append("receiverId", receiverId)
                .append("privacy", privacy)
                .append("message", message)
                .append("likes", likes)
                .append("songId",songId);
//                .append("comments", comments);
        return getObjectResponse(addPostDoc, postCollection);
    }

    private Response<Object> getObjectResponse(Document addPostDoc, MongoCollection<Document> postCollection) {
        try {
            postCollection.insertOne(addPostDoc);
            return Response.ok();
        } catch (MongoWriteException e) {
      // todo: user already exists
            LOG.error( "get object response: {}", e.getClass().getName() + ": " + e.getMessage() );
            return Response.forStatus(Status.CONFLICT);
        }
    }

    // Utility function for getMyFeed()
    // retrieves all friends of the specified user
    public List<Friend> getFriends(String name) {
        List<Friend> friendList = new ArrayList<>();
        try{
            Document queryFriends = new Document("name", name);
            Document inputUser = userCollection.find(queryFriends).iterator().next();
            friendList = (List<Friend>) inputUser.get("friends");
            return friendList;
        } catch (Exception e) {
            LOG.error (" getFriends {}", e.getClass().getName() + ": " + e.getMessage() );
        }
        return friendList;
    }

    public void deletePost(ObjectId postId) {
        postCollection = database.getCollection(this.config.getString("mongo.collection_post"));

    }

    public List<Post> getPostsWriter(ObjectId userId) {
        postCollection = database.getCollection(this.config.getString("mongo.collection_post"));
        List<Post> postList = new ArrayList<>();
        try {
            Document queryId = new Document("writerId", userId);
            getPostListFromQuery(postList, queryId);
        } catch (Exception e) {
            LOG.error( "getPostsWriter:  {}", e.getClass().getName() + ": " + e.getMessage() );
        }
        return postList;
    }

    private void getPostListFromQuery(List<Post> postList, Document queryId) {
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
            postList.add(post);
        }
    }

    public List<Post> getPostsReceiver(ObjectId userId) {
        postCollection = database.getCollection(this.config.getString("mongo.collection_post"));
        List<Post> postList = new ArrayList<>();
        try {
            Document queryId = new Document("receiverId", userId);
            getPostListFromQuery(postList, queryId);
        } catch (Exception e) {
            LOG.error( "getPostsReceiver: {}", e.getClass().getName() + ": " + e.getMessage() );
        }
        return postList;
    }

    // getFeed(name) - retrieves all posts written by the user and the user's friends
    public List<Post> getMyFeed(String name) {

        List<Post> postList = new ArrayList<>();
        ObjectId userId = userCollection.find(new Document("name", name)).iterator().next().getObjectId("_id");
        try {
            List<Friend> friendList = getFriends(name);
            if (friendList != null) {
                // iterate through each Friend of the User
                while (friendList.iterator().hasNext()) {
                    ObjectId friendUserId = friendList.iterator().next().userId();
                    List<Post> friendPosts = getPostsWriter(friendUserId);

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
            List<Post> userPosts = getPostsWriter(userId);
            getPostList(postList, userPosts);
        } catch (Exception e) {
            LOG.error( "getMyFeed: {}", e.getClass().getName() + ": " + e.getMessage() );
        }
        LOG.info("postList writer: " + postList.iterator().next().writerId() + " message: " + postList.iterator().next().message());
        return postList;
    }

    @Override
    public List<Post> getPublicFeed(String name) {
        userCollection = database.getCollection(this.config.getString("mongo.collection_user"));
        List<Post> writerPostList = new ArrayList<>();
        List<Post> receiverPostList = new ArrayList<>();
        ObjectId userId = userCollection.find(new Document("name", name)).iterator().next().getObjectId("_id");

        List<Post> userPostsWriter = getPostsWriter(userId);
        List<Post> userPostsReceiver = getPostsWriter(userId);
        try {
            getPostList(writerPostList, userPostsWriter);
            getPostList(receiverPostList, userPostsReceiver);
        } catch (Exception e) {
            LOG.error( "getPublicFeed: {}", e.getClass().getName() + ": " + e.getMessage() );
        }

        for (Post post : receiverPostList) {
            writerPostList.add(post);
        }
        return writerPostList;
    }

    private void getPostList(List<Post> postList, List<Post> userPostsWriter) {
        int size1 = userPostsWriter.size();
        Iterator<Post> userPostIterator1 = userPostsWriter.iterator();
        for (int i = 0; i < size1; i++) {
            Post userPost = userPostIterator1.next();
            postList.add(
                    new PostBuilder()
                            .id(userPost.id())
                            .writerId(userPost.writerId())
                            .receiverId(userPost.receiverId())
                            .message(userPost.message())
                            .build()
            );
        }
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
            LOG.error("getAllPosts: {}", e.getClass().getName() + ": " + e.getMessage() );
        }
        finally {
            cursor.close();
        }
        return postEntries;
    }

    // get only '_id' from User's 'name'
    public ObjectId getUserId(String name) {
        ObjectId userId = userCollection.find(new Document("name", name)).iterator().next().getObjectId("_id");
        if (userId != null) LOG.info("userId: {}", userId);
        return userId;
    }
}
