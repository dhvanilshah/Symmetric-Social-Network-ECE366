package cool.disc.server.store.post;

import com.spotify.apollo.Response;
import cool.disc.server.model.Friend;
import cool.disc.server.model.Post;

import java.util.List;

public interface PostStore {
    Response<Object> addPost(Post newPost, String user_id);
    List<Friend> getFriends(String id);
    List<Post> getPostsWriter(String userId);
    List<Post> getPostsReceiver(String userId);
    List<Post> getMyFeed(String name);
    List<Post> getPublicFeed(String name);

    List<Post> getAllPosts();
}