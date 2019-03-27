package cool.disc.server.store.post;

import com.mongodb.client.*;
import cool.disc.server.model.Post;
import org.bson.Document;
import org.bson.types.ObjectId;
import cool.disc.server.model.PostBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PostStoreController implements PostStore {


    private MongoClient mongoClient = MongoClients.create();
    private MongoDatabase database = mongoClient.getDatabase("discbase");
    private MongoCollection<Document> postCollection = database.getCollection("post");
    private MongoCollection<Document> userCollection = database.getCollection("user");

    public PostStoreController() {
    }

    @Override
    public void addPost(String receiverId, String writerId, String message, Integer isPrivate) {
        ObjectId new_post_id = new ObjectId();
        Document new_post = new Document("_id", new_post_id)
                .append("receiverId", receiverId)
                .append("writerId", writerId)
                .append("message", message)
                .append("isPrivate", isPrivate);
        postCollection.insertOne(new_post);
    }


//CHANGE TO getPostto
    @Override
    public List<Post> getPostFrom(final String first_name, final String last_name) {

        Document firstNameRegQuery = new Document("$regex", "^(?)" + Pattern.quote(first_name));
        firstNameRegQuery.append("$options", "i");
        Document lastNameRegQuery = new Document("$regex", "^(?)" + Pattern.quote(last_name));
        lastNameRegQuery.append("$options", "i");
        Document findUserIdQuery = new Document("firstname", firstNameRegQuery);
        findUserIdQuery.append("lastname", lastNameRegQuery);
        MongoCursor<Document> userCursor = userCollection.find(findUserIdQuery).iterator();
        Document firstMatchingUser = userCursor.next();
        System.out.println("**getpostfrom() ** USERID QUERY RESULT: " + firstMatchingUser);
        ObjectId userId = firstMatchingUser.getObjectId("_id");

        System.out.println("Found entry! - "+userId.toHexString());

        Document postQuery = new Document("writerId", userId);
        MongoCursor<Document> iterable = postCollection.find(postQuery).iterator();

        List<Post> postList = new ArrayList<Post>();
        while(iterable.hasNext()){
            Document postDoc = iterable.next();
            System.out.println("****DOCUMENT RESULT: " + postDoc);
            ObjectId id = postDoc.getObjectId( "_id" );
            String receiverId = postDoc.getObjectId("receiverId").toString();
            String writerId = postDoc.getObjectId("writerId").toString();
            String message = postDoc.getString("message");
            Integer isPrivate = postDoc.getInteger("isPrivate");

            Post post = new PostBuilder()
                    .id(id.toHexString())
                    .receiverId(receiverId)
                    .writerId(writerId)
                    .message(message)
                    .isPrivate(isPrivate)
                    .build();
            postList.add(post);
        }
        System.out.println(postList);
        return postList;
    }

    @Override
    public List<Post> getPostTo(final String first_name, final String last_name) {
        Document firstNameRegQuery = new Document("$regex", "^(?)" + Pattern.quote(first_name));
        firstNameRegQuery.append("$options", "i");
        Document lastNameRegQuery = new Document("$regex", "^(?)" + Pattern.quote(last_name));
        lastNameRegQuery.append("$options", "i");
        Document findUserIdQuery = new Document("firstname", firstNameRegQuery);
        findUserIdQuery.append("lastname", lastNameRegQuery);
        MongoCursor<Document> userCursor = userCollection.find(findUserIdQuery).iterator();
        Document firstMatchingUser = userCursor.next();
        ObjectId userId = firstMatchingUser.getObjectId("_id");

        System.out.println("Found entry! - "+userId.toHexString());

        Document postQuery = new Document("receiverId", userId);
        MongoCursor<Document> iterable = (MongoCursor<Document>) postCollection.find(postQuery);
        List<Post> postList = new ArrayList<Post>();
        while(iterable.hasNext()) {
            Document postDoc = iterable.next();
            ObjectId id = postDoc.getObjectId( "_id" );
            String receiverId = postDoc.getObjectId("receiverId").toString();
            String writerId = postDoc.getObjectId("writerId").toString();
            String message = postDoc.getString("message");
            Integer isPrivate = postDoc.getInteger("isPrivate");

            Post post = new PostBuilder()
                    .id(id.toHexString())
                    .receiverId(receiverId)
                    .writerId(writerId)
                    .message(message)
                    .isPrivate(isPrivate)
                    .build();
            postList.add(post);
        }
        System.out.println(postList);
        return postList;
    }

    @Override
    public List<Post> getAllPosts() {
        MongoCursor<Document> cursor = postCollection.find().iterator();
        List<Post> postEntries = new ArrayList<Post>();
        try{
            while(cursor.hasNext()) {
                Document nextentry = cursor.next();
                Post post = new PostBuilder()
                        .id(nextentry.getObjectId("_id").toString())
                        .receiverId(nextentry.getObjectId("receiverId").toString())
                        .writerId(nextentry.getObjectId("writerId").toString())
                        .message(nextentry.getString("message"))
                        .isPrivate(nextentry.getInteger("isPrivate"))
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
