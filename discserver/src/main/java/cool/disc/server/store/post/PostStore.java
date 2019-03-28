package cool.disc.server.store.post;

import cool.disc.server.model.Post;
import java.util.List;

public interface PostStore {
    void addPost(String to_user_id, String from_user_id, String message, Integer isPrivate);
    List<Post> getPostFrom(String first_name, String last_name);
    List<Post> getPostTo(String first_name, String last_name);
    List<Post> getAllPosts();
}