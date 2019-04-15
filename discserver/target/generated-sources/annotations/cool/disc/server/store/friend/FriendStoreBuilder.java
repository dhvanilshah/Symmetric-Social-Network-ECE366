package cool.disc.server.store.friend;

import cool.disc.server.model.Friend;
import io.norberg.automatter.AutoMatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Generated;
import org.bson.types.ObjectId;

@Generated("io.norberg.automatter.processor.AutoMatterProcessor")
public final class FriendStoreBuilder {
  private ObjectId getUserId;

  private ObjectId getId;

  private Integer getScore;

  private List<Friend> getFriendList;

  private Friend addFriend;

  private Friend removeFriend;

  public FriendStoreBuilder() {
  }

  private FriendStoreBuilder(FriendStore v) {
    this.getUserId = v.getUserId();
    this.getId = v.getId();
    this.getScore = v.getScore();
    List<Friend> _getFriendList = v.getFriendList();
    this.getFriendList = (_getFriendList == null) ? null : new ArrayList<Friend>(_getFriendList);
    this.addFriend = v.addFriend();
    this.removeFriend = v.removeFriend();
  }

  private FriendStoreBuilder(FriendStoreBuilder v) {
    this.getUserId = v.getUserId;
    this.getId = v.getId;
    this.getScore = v.getScore;
    this.getFriendList = (v.getFriendList == null) ? null : new ArrayList<Friend>(v.getFriendList);
    this.addFriend = v.addFriend;
    this.removeFriend = v.removeFriend;
  }

  public ObjectId getUserId() {
    return getUserId;
  }

  public FriendStoreBuilder getUserId(ObjectId getUserId) {
    if (getUserId == null) {
      throw new NullPointerException("getUserId");
    }
    this.getUserId = getUserId;
    return this;
  }

  public ObjectId getId() {
    return getId;
  }

  public FriendStoreBuilder getId(ObjectId getId) {
    if (getId == null) {
      throw new NullPointerException("getId");
    }
    this.getId = getId;
    return this;
  }

  public Integer getScore() {
    return getScore;
  }

  public FriendStoreBuilder getScore(Integer getScore) {
    if (getScore == null) {
      throw new NullPointerException("getScore");
    }
    this.getScore = getScore;
    return this;
  }

  public List<Friend> getFriendList() {
    if (this.getFriendList == null) {
      this.getFriendList = new ArrayList<Friend>();
    }
    return getFriendList;
  }

  public FriendStoreBuilder getFriendList(List<? extends Friend> getFriendList) {
    return getFriendList((Collection<? extends Friend>) getFriendList);
  }

  public FriendStoreBuilder getFriendList(Collection<? extends Friend> getFriendList) {
    if (getFriendList == null) {
      throw new NullPointerException("getFriendList");
    }
    for (Friend item : getFriendList) {
      if (item == null) {
        throw new NullPointerException("getFriendList: null item");
      }
    }
    this.getFriendList = new ArrayList<Friend>(getFriendList);
    return this;
  }

  public FriendStoreBuilder getFriendList(Iterable<? extends Friend> getFriendList) {
    if (getFriendList == null) {
      throw new NullPointerException("getFriendList");
    }
    if (getFriendList instanceof Collection) {
      return getFriendList((Collection<? extends Friend>) getFriendList);
    }
    return getFriendList(getFriendList.iterator());
  }

  public FriendStoreBuilder getFriendList(Iterator<? extends Friend> getFriendList) {
    if (getFriendList == null) {
      throw new NullPointerException("getFriendList");
    }
    this.getFriendList = new ArrayList<Friend>();
    while (getFriendList.hasNext()) {
      Friend item = getFriendList.next();
      if (item == null) {
        throw new NullPointerException("getFriendList: null item");
      }
      this.getFriendList.add(item);
    }
    return this;
  }

  @SafeVarargs
  public final FriendStoreBuilder getFriendList(Friend... getFriendList) {
    if (getFriendList == null) {
      throw new NullPointerException("getFriendList");
    }
    return getFriendList(Arrays.asList(getFriendList));
  }

  public Friend addFriend() {
    return addFriend;
  }

  public FriendStoreBuilder addFriend(Friend addFriend) {
    if (addFriend == null) {
      throw new NullPointerException("addFriend");
    }
    this.addFriend = addFriend;
    return this;
  }

  public Friend removeFriend() {
    return removeFriend;
  }

  public FriendStoreBuilder removeFriend(Friend removeFriend) {
    if (removeFriend == null) {
      throw new NullPointerException("removeFriend");
    }
    this.removeFriend = removeFriend;
    return this;
  }

  public FriendStore build() {
    List<Friend> _getFriendList = (getFriendList != null) ? Collections.unmodifiableList(new ArrayList<Friend>(getFriendList)) : Collections.<Friend>emptyList();
    return new Value(getUserId, getId, getScore, _getFriendList, addFriend, removeFriend);
  }

  public static FriendStoreBuilder from(FriendStore v) {
    return new FriendStoreBuilder(v);
  }

  public static FriendStoreBuilder from(FriendStoreBuilder v) {
    return new FriendStoreBuilder(v);
  }

  private static final class Value implements FriendStore {
    private final ObjectId getUserId;

    private final ObjectId getId;

    private final Integer getScore;

    private final List<Friend> getFriendList;

    private final Friend addFriend;

    private final Friend removeFriend;

    private Value(@AutoMatter.Field("getUserId") ObjectId getUserId,
        @AutoMatter.Field("getId") ObjectId getId, @AutoMatter.Field("getScore") Integer getScore,
        @AutoMatter.Field("getFriendList") List<Friend> getFriendList,
        @AutoMatter.Field("addFriend") Friend addFriend,
        @AutoMatter.Field("removeFriend") Friend removeFriend) {
      if (getUserId == null) {
        throw new NullPointerException("getUserId");
      }
      if (getId == null) {
        throw new NullPointerException("getId");
      }
      if (getScore == null) {
        throw new NullPointerException("getScore");
      }
      if (addFriend == null) {
        throw new NullPointerException("addFriend");
      }
      if (removeFriend == null) {
        throw new NullPointerException("removeFriend");
      }
      this.getUserId = getUserId;
      this.getId = getId;
      this.getScore = getScore;
      this.getFriendList = (getFriendList != null) ? getFriendList : Collections.<Friend>emptyList();
      this.addFriend = addFriend;
      this.removeFriend = removeFriend;
    }

    @AutoMatter.Field
    @Override
    public ObjectId getUserId() {
      return getUserId;
    }

    @AutoMatter.Field
    @Override
    public ObjectId getId() {
      return getId;
    }

    @AutoMatter.Field
    @Override
    public Integer getScore() {
      return getScore;
    }

    @AutoMatter.Field
    @Override
    public List<Friend> getFriendList() {
      return getFriendList;
    }

    @AutoMatter.Field
    @Override
    public Friend addFriend() {
      return addFriend;
    }

    @AutoMatter.Field
    @Override
    public Friend removeFriend() {
      return removeFriend;
    }

    public FriendStoreBuilder builder() {
      return new FriendStoreBuilder(this);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof FriendStore)) {
        return false;
      }
      final FriendStore that = (FriendStore) o;
      if (getUserId != null ? !getUserId.equals(that.getUserId()) : that.getUserId() != null) {
        return false;
      }
      if (getId != null ? !getId.equals(that.getId()) : that.getId() != null) {
        return false;
      }
      if (getScore != null ? !getScore.equals(that.getScore()) : that.getScore() != null) {
        return false;
      }
      if (getFriendList != null ? !getFriendList.equals(that.getFriendList()) : that.getFriendList() != null) {
        return false;
      }
      if (addFriend != null ? !addFriend.equals(that.addFriend()) : that.addFriend() != null) {
        return false;
      }
      if (removeFriend != null ? !removeFriend.equals(that.removeFriend()) : that.removeFriend() != null) {
        return false;
      }
      return true;
    }

    @Override
    public int hashCode() {
      int result = 1;
      long temp;
      result = 31 * result + (this.getUserId != null ? this.getUserId.hashCode() : 0);
      result = 31 * result + (this.getId != null ? this.getId.hashCode() : 0);
      result = 31 * result + (this.getScore != null ? this.getScore.hashCode() : 0);
      result = 31 * result + (this.getFriendList != null ? this.getFriendList.hashCode() : 0);
      result = 31 * result + (this.addFriend != null ? this.addFriend.hashCode() : 0);
      result = 31 * result + (this.removeFriend != null ? this.removeFriend.hashCode() : 0);
      return result;
    }

    @Override
    public String toString() {
      return "FriendStore{" +
      "getUserId=" + getUserId +
      ", getId=" + getId +
      ", getScore=" + getScore +
      ", getFriendList=" + getFriendList +
      ", addFriend=" + addFriend +
      ", removeFriend=" + removeFriend +
      '}';
    }
  }
}
