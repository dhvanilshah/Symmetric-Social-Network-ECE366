package cool.disc.server.test.store.post;

import com.spotify.apollo.Response;
import cool.disc.server.test.model.Post;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public interface PostStore {
    Response<Object> addPost(Post newPost, String user_id);
    ArrayList<Document> getFriends(ObjectId id);
    List<Post> getPostsWriter(ObjectId userId);
    List<Post> getPostsReceiver(ObjectId userId);
    List<Post> getMyFeed(String name);
    List<Post> getPublicFeed(String username);
}