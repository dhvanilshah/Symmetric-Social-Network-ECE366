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
import cool.disc.server.model.Post;
import cool.disc.server.model.PostBuilder;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
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
    private MongoCollection<Document> songCollection;

    public PostStoreController() {
        this.config = ConfigFactory.load("discserver.conf");

        // get login info from config
        String uri1 = this.config.getString("mongo.uri");
        String username = this.config.getString("mongo.username");
        String password = this.config.getString("mongo.password");
        String host = this.config.getString("mongo.host");
        String uriString = uri1 + username + password;

//         initialize db driver
        uri = new MongoClientURI(uri1);
        dbClient = new com.mongodb.MongoClient(uri);
        String databaseString = this.config.getString("mongo.database");
        database = dbClient.getDatabase(databaseString);

//      localhost for testing
        MongoClient dbClient = new MongoClient("localhost", 27017);
        database = dbClient.getDatabase("discbase");

        // database
        String userdb = this.config.getString("mongo.collection_user");
        String postdb = this.config.getString("mongo.collection_post");
        String songdb = this.config.getString("mongo.collection_song");
        userCollection = database.getCollection(userdb);
        postCollection = database.getCollection(postdb);
        songCollection = database.getCollection(songdb);
    }

    // (GET) addPost
    @Override
    public Response<Object> addPost(Post newPost, String user_id) {

        // parse data from the payload
        ObjectId newId = new ObjectId();
        ObjectId writerId = new ObjectId(user_id);
        ObjectId receiverId;
        if ("self".equals(newPost.receiverIdString())) {
            receiverId = writerId;
        } else {
            receiverId = new ObjectId(newPost.receiverIdString());
        }
        Integer privacy = newPost.privacy();
        String message = newPost.message();
        Integer likes = newPost.likes();

        ObjectId songId = new ObjectId(newPost.songIdString());

        // create document to insert
        Document addPostDoc = new Document("_id", newId)
                .append("writerId", writerId)
                .append("receiverId", receiverId)
                .append("privacy", privacy)
                .append("message", message)
                .append("likes", likes)
                .append("songId", songId)
                .append("dateCreated", new Date());
        return getObjectResponse(addPostDoc, postCollection);
    }

    private Response<Object> getObjectResponse(Document addPostDoc, MongoCollection<Document> postCollection) {
        try {
            postCollection.insertOne(addPostDoc);
            return Response.ok();
        } catch (MongoWriteException e) {
            // todo: user already exists
            LOG.error("get object response: {}", e.getClass().getName() + ": " + e.getMessage());
            return Response.forStatus(Status.CONFLICT);
        }
    }

    // Utility function for getMyFeed()
    // retrieves all friends of the specified user
    public ArrayList<Document> getFriends(ObjectId userId) {
        ArrayList<Document> friendList = null;
        try {
            Document queryFriends = new Document("_id", userId);
            Document inputUser = userCollection.find(queryFriends).iterator().next();
            friendList = (ArrayList<Document>) inputUser.get("friends");
            return friendList;
        } catch (Exception e) {
            LOG.error(" getFriends error: {}", e.getClass().getName() + ": " + e.getMessage());
        }
        return friendList;
    }

    public List<Post> getPostsWriter(ObjectId userId) {
        List<Post> postList = new ArrayList<>();
        try {
            Document queryId = new Document("writerId", userId);
            getPostListFromQuery(postList, queryId);
        } catch (Exception e) {
            LOG.error("getPostsWriter:  {}", e.getClass().getName() + ": " + e.getMessage());
        }
        return postList;
    }

    private void getPostListFromQuery(List<Post> postList, Document queryId) {
        MongoCursor<Document> matchingPosts = postCollection.find(queryId).iterator();
        while (matchingPosts.hasNext()) {
            Document postDoc = matchingPosts.next();
            Document songDoc = songCollection.find(new Document("_id", postDoc.getObjectId("songId"))).first();
            postList.add(
                    new PostBuilder()
                            .id(postDoc.getObjectId("_id"))
                            .writerId(postDoc.getObjectId("writerId"))
                            .receiverId(postDoc.getObjectId("receiverId"))
                            .message(postDoc.getString("message"))
                            .songId(postDoc.getObjectId("songId"))
                            .title(songDoc.getString("title"))
                            .songUrl(songDoc.getString("songUrl"))
                            .artist(songDoc.getString("artist"))
                            .albumImageUrl(songDoc.getString("albumImageUrl"))
                            .privacy(postDoc.getInteger("privacy"))
                            .build()
            );
        }
    }

    public List<Post> getPostsReceiver(ObjectId userId) {
        List<Post> postList = new ArrayList<>();
        try {
            Document queryId = new Document("receiverId", userId);
            getPostListFromQuery(postList, queryId);
        } catch (Exception e) {
            LOG.error("getPostsReceiver: {}", e.getClass().getName() + ": " + e.getMessage());
        }

        for(int i = 0; i < postList.size(); i++){
            Post currentPost = postList.get(i);
            if(currentPost.writerId().equals(currentPost.receiverId())){
                postList.remove(currentPost);
            }
        }

        return postList;
    }

    // getFeed(name) - retrieves all posts written by the user and the user's friends
    public List<Post> getMyFeed(String user_id) {

        List<Post> postList = new ArrayList<>();
        ObjectId userId = new ObjectId(user_id);
        try {
            ArrayList<Document> friendList = getFriends(userId);
            if (!friendList.isEmpty()) {
                // iterate through each Friend of the User
                for (Document friend : friendList) {

                    List<Post> friendPosts = getPostsWriter(friend.getObjectId("userId"));

                    // iterate through selected Friend's posts
                    Post friendPost = friendPosts.iterator().next();
                    System.out.println("got a friend. post message: " + friendPost.message());

                    Document songDoc = songCollection.find(new Document("_id", friendPost.songId())).first();
                    postList.add(
                            new PostBuilder()
                                    .id(friendPost.id())
                                    .writerId(friendPost.writerId())
                                    .receiverId(friendPost.receiverId())
                                    .message(friendPost.message())
                                    .songId(friendPost.songId())
                                    .title(songDoc.getString("title"))
                                    .songUrl(songDoc.getString("songUrl"))
                                    .artist(songDoc.getString("artist"))
                                    .albumImageUrl(songDoc.getString("albumImageUrl"))
                                    .privacy(friendPost.privacy())
                                    .build()
                    );
                }
            }
            List<Post> userPosts = getPostsWriter(userId);
            getPostList(postList, userPosts);
        } catch (Exception e) {
            LOG.error("getMyFeed error: {}", e.getClass().getName() + ": " + e.getMessage());
        }
        LOG.info("postList writer: " + postList.iterator().next().writerId() + " message: " + postList.iterator().next().message());
        return postList;
    }

    @Override
    public List<Post> getPublicFeed(String userId) {
        List<Post> writerPostList = new ArrayList<>();
        List<Post> receiverPostList = new ArrayList<>();

        List<Post> userPostsWriter = getPostsWriter(new ObjectId(userId));
        List<Post> userPostsReceiver = getPostsReceiver(new ObjectId(userId));
        try {
            getPostList(writerPostList, userPostsWriter);
            getPostList(receiverPostList, userPostsReceiver);
        } catch (Exception e) {
            LOG.error("getPublicFeed error: {}", e.getClass().getName() + ": " + e.getMessage());
        }

        for (Post post : receiverPostList) {
            writerPostList.add(post);
        }
        List<Post> finalPostList = writerPostList;
        return finalPostList;
    }

    private void getPostList(List<Post> postList, List<Post> userPostsWriter) {
        int size1 = userPostsWriter.size();
        Iterator<Post> userPostIterator1 = userPostsWriter.iterator();
        for (int i = 0; i < size1; i++) {
            Post userPost = userPostIterator1.next();
            postList.add(userPost);
        }
    }

}