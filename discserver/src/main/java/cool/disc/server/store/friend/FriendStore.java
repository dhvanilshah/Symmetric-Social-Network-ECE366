package cool.disc.server.store.friend;

import cool.disc.server.model.Friend;
import io.norberg.automatter.AutoMatter;

import java.util.List;

@AutoMatter
public interface FriendStore {
    List<Friend> getFriendList();
    Friend addFriend();
    Friend removeFriend();
}
