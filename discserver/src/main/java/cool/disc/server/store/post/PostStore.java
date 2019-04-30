package cool.disc.server.store.post;

import com.spotify.apollo.Response;
import cool.disc.server.model.Friend;
import cool.disc.server.model.Post;
import org.bson.types.ObjectId;

import java.util.List;

public interface PostStore {
    //    Post addPost(ObjectId writerId, ObjectId receiverId, String message);
    Response<Object> addPost(Post newPost);
    List<Friend> getFriends(String id);
    List<Post> getPostsWriter(ObjectId userId);
    List<Post> getPostsReceiver(ObjectId userId);
    List<Post> getMyFeed(String name);
    List<Post> getPublicFeed(String name);

    ObjectId getUserId(String name);
    List<Post> getAllPosts();
}