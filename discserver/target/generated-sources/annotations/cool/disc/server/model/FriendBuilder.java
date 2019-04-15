package cool.disc.server.model;

import io.norberg.automatter.AutoMatter;
import javax.annotation.Generated;
import javax.annotation.Nullable;
import org.bson.types.ObjectId;

@Generated("io.norberg.automatter.processor.AutoMatterProcessor")
public final class FriendBuilder {
  private ObjectId userId;

  private Integer score;

  public FriendBuilder() {
  }

  private FriendBuilder(Friend v) {
    this.userId = v.userId();
    this.score = v.score();
  }

  private FriendBuilder(FriendBuilder v) {
    this.userId = v.userId;
    this.score = v.score;
  }

  public ObjectId userId() {
    return userId;
  }

  public FriendBuilder userId(ObjectId userId) {
    if (userId == null) {
      throw new NullPointerException("userId");
    }
    this.userId = userId;
    return this;
  }

  public Integer score() {
    return score;
  }

  public FriendBuilder score(@Nullable Integer score) {
    this.score = score;
    return this;
  }

  public Friend build() {
    return new Value(userId, score);
  }

  public static FriendBuilder from(Friend v) {
    return new FriendBuilder(v);
  }

  public static FriendBuilder from(FriendBuilder v) {
    return new FriendBuilder(v);
  }

  private static final class Value implements Friend {
    private final ObjectId userId;

    private final Integer score;

    private Value(@AutoMatter.Field("userId") ObjectId userId,
        @AutoMatter.Field("score") Integer score) {
      if (userId == null) {
        throw new NullPointerException("userId");
      }
      this.userId = userId;
      this.score = score;
    }

    @AutoMatter.Field
    @Override
    public ObjectId userId() {
      return userId;
    }

    @AutoMatter.Field
    @Override
    public Integer score() {
      return score;
    }

    public FriendBuilder builder() {
      return new FriendBuilder(this);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof Friend)) {
        return false;
      }
      final Friend that = (Friend) o;
      if (userId != null ? !userId.equals(that.userId()) : that.userId() != null) {
        return false;
      }
      if (score != null ? !score.equals(that.score()) : that.score() != null) {
        return false;
      }
      return true;
    }

    @Override
    public int hashCode() {
      int result = 1;
      long temp;
      result = 31 * result + (this.userId != null ? this.userId.hashCode() : 0);
      result = 31 * result + (this.score != null ? this.score.hashCode() : 0);
      return result;
    }

    @Override
    public String toString() {
      return "Friend{" +
      "userId=" + userId +
      ", score=" + score +
      '}';
    }
  }
}
