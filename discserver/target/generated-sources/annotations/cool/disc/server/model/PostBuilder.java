package cool.disc.server.model;

import io.norberg.automatter.AutoMatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Generated;
import javax.annotation.Nullable;
import org.bson.types.ObjectId;

@Generated("io.norberg.automatter.processor.AutoMatterProcessor")
public final class PostBuilder {
  private ObjectId id;

  private ObjectId writerId;

  private ObjectId receiverId;

  private String message;

  private Integer privacy;

  private Integer likes;

  private ObjectId songId;

  private List<String> comments;

  public PostBuilder() {
  }

  private PostBuilder(Post v) {
    this.id = v.id();
    this.writerId = v.writerId();
    this.receiverId = v.receiverId();
    this.message = v.message();
    this.privacy = v.privacy();
    this.likes = v.likes();
    this.songId = v.songId();
    List<String> _comments = v.comments();
    this.comments = (_comments == null) ? null : new ArrayList<String>(_comments);
  }

  private PostBuilder(PostBuilder v) {
    this.id = v.id;
    this.writerId = v.writerId;
    this.receiverId = v.receiverId;
    this.message = v.message;
    this.privacy = v.privacy;
    this.likes = v.likes;
    this.songId = v.songId;
    this.comments = (v.comments == null) ? null : new ArrayList<String>(v.comments);
  }

  public ObjectId id() {
    return id;
  }

  public PostBuilder id(ObjectId id) {
    if (id == null) {
      throw new NullPointerException("id");
    }
    this.id = id;
    return this;
  }

  public ObjectId writerId() {
    return writerId;
  }

  public PostBuilder writerId(ObjectId writerId) {
    if (writerId == null) {
      throw new NullPointerException("writerId");
    }
    this.writerId = writerId;
    return this;
  }

  public ObjectId receiverId() {
    return receiverId;
  }

  public PostBuilder receiverId(ObjectId receiverId) {
    if (receiverId == null) {
      throw new NullPointerException("receiverId");
    }
    this.receiverId = receiverId;
    return this;
  }

  public String message() {
    return message;
  }

  public PostBuilder message(String message) {
    if (message == null) {
      throw new NullPointerException("message");
    }
    this.message = message;
    return this;
  }

  public Integer privacy() {
    return privacy;
  }

  public PostBuilder privacy(@Nullable Integer privacy) {
    this.privacy = privacy;
    return this;
  }

  public Integer likes() {
    return likes;
  }

  public PostBuilder likes(@Nullable Integer likes) {
    this.likes = likes;
    return this;
  }

  public ObjectId songId() {
    return songId;
  }

  public PostBuilder songId(@Nullable ObjectId songId) {
    this.songId = songId;
    return this;
  }

  public List<String> comments() {
    return comments;
  }

  public PostBuilder comments(List<? extends String> comments) {
    return comments((Collection<? extends String>) comments);
  }

  public PostBuilder comments(Collection<? extends String> comments) {
    if (comments == null) {
      this.comments = null;
      return this;
    }
    this.comments = new ArrayList<String>(comments);
    return this;
  }

  public PostBuilder comments(Iterable<? extends String> comments) {
    if (comments == null) {
      this.comments = null;
      return this;
    }
    if (comments instanceof Collection) {
      return comments((Collection<? extends String>) comments);
    }
    return comments(comments.iterator());
  }

  public PostBuilder comments(Iterator<? extends String> comments) {
    if (comments == null) {
      this.comments = null;
      return this;
    }
    this.comments = new ArrayList<String>();
    while (comments.hasNext()) {
      String item = comments.next();
      this.comments.add(item);
    }
    return this;
  }

  @SafeVarargs
  public final PostBuilder comments(String... comments) {
    if (comments == null) {
      this.comments = null;
      return this;
    }
    return comments(Arrays.asList(comments));
  }

  public PostBuilder addComment(String comment) {
    if (this.comments == null) {
      this.comments = new ArrayList<String>();
    }
    comments.add(comment);
    return this;
  }

  public Post build() {
    List<String> _comments = (comments != null) ? Collections.unmodifiableList(new ArrayList<String>(comments)) : null;
    return new Value(id, writerId, receiverId, message, privacy, likes, songId, _comments);
  }

  public static PostBuilder from(Post v) {
    return new PostBuilder(v);
  }

  public static PostBuilder from(PostBuilder v) {
    return new PostBuilder(v);
  }

  private static final class Value implements Post {
    private final ObjectId id;

    private final ObjectId writerId;

    private final ObjectId receiverId;

    private final String message;

    private final Integer privacy;

    private final Integer likes;

    private final ObjectId songId;

    private final List<String> comments;

    private Value(@AutoMatter.Field("id") ObjectId id,
        @AutoMatter.Field("writerId") ObjectId writerId,
        @AutoMatter.Field("receiverId") ObjectId receiverId,
        @AutoMatter.Field("message") String message, @AutoMatter.Field("privacy") Integer privacy,
        @AutoMatter.Field("likes") Integer likes, @AutoMatter.Field("songId") ObjectId songId,
        @AutoMatter.Field("comments") List<String> comments) {
      if (id == null) {
        throw new NullPointerException("id");
      }
      if (writerId == null) {
        throw new NullPointerException("writerId");
      }
      if (receiverId == null) {
        throw new NullPointerException("receiverId");
      }
      if (message == null) {
        throw new NullPointerException("message");
      }
      this.id = id;
      this.writerId = writerId;
      this.receiverId = receiverId;
      this.message = message;
      this.privacy = privacy;
      this.likes = likes;
      this.songId = songId;
      this.comments = comments;
    }

    @AutoMatter.Field
    @Override
    public ObjectId id() {
      return id;
    }

    @AutoMatter.Field
    @Override
    public ObjectId writerId() {
      return writerId;
    }

    @AutoMatter.Field
    @Override
    public ObjectId receiverId() {
      return receiverId;
    }

    @AutoMatter.Field
    @Override
    public String message() {
      return message;
    }

    @AutoMatter.Field
    @Override
    public Integer privacy() {
      return privacy;
    }

    @AutoMatter.Field
    @Override
    public Integer likes() {
      return likes;
    }

    @AutoMatter.Field
    @Override
    public ObjectId songId() {
      return songId;
    }

    @AutoMatter.Field
    @Override
    public List<String> comments() {
      return comments;
    }

    public PostBuilder builder() {
      return new PostBuilder(this);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof Post)) {
        return false;
      }
      final Post that = (Post) o;
      if (id != null ? !id.equals(that.id()) : that.id() != null) {
        return false;
      }
      if (writerId != null ? !writerId.equals(that.writerId()) : that.writerId() != null) {
        return false;
      }
      if (receiverId != null ? !receiverId.equals(that.receiverId()) : that.receiverId() != null) {
        return false;
      }
      if (message != null ? !message.equals(that.message()) : that.message() != null) {
        return false;
      }
      if (privacy != null ? !privacy.equals(that.privacy()) : that.privacy() != null) {
        return false;
      }
      if (likes != null ? !likes.equals(that.likes()) : that.likes() != null) {
        return false;
      }
      if (songId != null ? !songId.equals(that.songId()) : that.songId() != null) {
        return false;
      }
      if (comments != null ? !comments.equals(that.comments()) : that.comments() != null) {
        return false;
      }
      return true;
    }

    @Override
    public int hashCode() {
      int result = 1;
      long temp;
      result = 31 * result + (this.id != null ? this.id.hashCode() : 0);
      result = 31 * result + (this.writerId != null ? this.writerId.hashCode() : 0);
      result = 31 * result + (this.receiverId != null ? this.receiverId.hashCode() : 0);
      result = 31 * result + (this.message != null ? this.message.hashCode() : 0);
      result = 31 * result + (this.privacy != null ? this.privacy.hashCode() : 0);
      result = 31 * result + (this.likes != null ? this.likes.hashCode() : 0);
      result = 31 * result + (this.songId != null ? this.songId.hashCode() : 0);
      result = 31 * result + (this.comments != null ? this.comments.hashCode() : 0);
      return result;
    }

    @Override
    public String toString() {
      return "Post{" +
      "id=" + id +
      ", writerId=" + writerId +
      ", receiverId=" + receiverId +
      ", message=" + message +
      ", privacy=" + privacy +
      ", likes=" + likes +
      ", songId=" + songId +
      ", comments=" + comments +
      '}';
    }
  }
}
