package cool.disc.server.store.user;

import com.spotify.apollo.Response;
import cool.disc.server.model.User;
import okio.ByteString;
import org.bson.types.ObjectId;

import java.util.List;

public interface UserStore {

    User addUser(String username, String name, String password, String service, String photo);
    List<User> getUser(String name);
    ObjectId getUserId(String name);
    String login(String username, String password);
}
