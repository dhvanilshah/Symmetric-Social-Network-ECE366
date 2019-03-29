package cool.disc.server.store.post;

import cool.disc.server.model.Post;
import cool.disc.server.model.User;

import java.util.List;

public interface PostStore {
    void addPost(String writerId, String receiverId, String message);
    public List<User> getFriends(String id);
    public List<Post> getPosts(final String userId);
    public List<Post> getFeed();

    // for testing
    List<Post> getAllPosts();
}