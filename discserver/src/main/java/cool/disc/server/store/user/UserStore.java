package cool.disc.server.store.user;

import cool.disc.server.model.User;
import org.bson.types.ObjectId;

import java.util.List;

public interface UserStore {

    //    User addUser(String username, String name, String password, String service, String photo);
    Integer addUser(User newUser);
    List<User> getUser(String name);
    ObjectId getUserId(String name);
    String login(String username, String password);
    String addFriend(String friend_id, String user_id);
    String handleRequest(String friend_id, String user_id, String action);
}
