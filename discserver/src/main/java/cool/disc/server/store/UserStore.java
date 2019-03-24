package cool.disc.server.store;

import cool.disc.server.model.User;

import java.util.List;

public interface UserStore {

    User addUser( String username, String name, String password, String service, String photo);
    List<User> getUser(String name);
}
