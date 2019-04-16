package cool.disc.server.store.friend;

import com.typesafe.config.Config;
import cool.disc.server.model.Friend;
import io.norberg.automatter.AutoMatter;
import org.bson.types.ObjectId;

import java.util.List;

public class FriendStoreController implements FriendStore {
    private final Config config;


    public FriendStoreController(final Config config) {
        this.config = config;
    }

    @Override
    public ObjectId getUserId() {

        return null;
    }

    @Override
    public ObjectId getId() {
        return null;
    }

    @Override
    public Integer getScore() {
        return null;
    }

    @Override
    public List<Friend> getFriendList() {
        return null;
    }

    @Override
    public Friend addFriend() {
        return null;
    }

    @Override
    public Friend removeFriend() {
        return null;
    }

}
