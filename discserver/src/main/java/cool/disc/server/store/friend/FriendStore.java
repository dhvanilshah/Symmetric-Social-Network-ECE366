package cool.disc.server.store.friend;

import cool.disc.server.model.Friend;
import io.norberg.automatter.AutoMatter;
import org.bson.types.ObjectId;

import java.util.List;

@AutoMatter
public interface FriendStore {
    ObjectId getUserId();

    ObjectId getId();
    Integer getScore();
    List<Friend> getFriendList();
    Friend addFriend();
    Friend removeFriend();
}
