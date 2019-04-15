package cool.disc.server.model;

import io.norberg.automatter.AutoMatter;
import java.util.Date;
import javax.annotation.Generated;
import javax.annotation.Nullable;

@Generated("io.norberg.automatter.processor.AutoMatterProcessor")
public final class CommentBuilder {
  private String id;

  private String writerId;

  private String postId;

  private Date date;

  private Integer likes;

  public CommentBuilder() {
  }

  private CommentBuilder(Comment v) {
    this.id = v.id();
    this.writerId = v.writerId();
    this.postId = v.postId();
    this.date = v.date();
    this.likes = v.likes();
  }

  private CommentBuilder(CommentBuilder v) {
    this.id = v.id;
    this.writerId = v.writerId;
    this.postId = v.postId;
    this.date = v.date;
    this.likes = v.likes;
  }

  public String id() {
    return id;
  }

  public CommentBuilder id(String id) {
    if (id == null) {
      throw new NullPointerException("id");
    }
    this.id = id;
    return this;
  }

  public String writerId() {
    return writerId;
  }

  public CommentBuilder writerId(String writerId) {
    if (writerId == null) {
      throw new NullPointerException("writerId");
    }
    this.writerId = writerId;
    return this;
  }

  public String postId() {
    return postId;
  }

  public CommentBuilder postId(String postId) {
    if (postId == null) {
      throw new NullPointerException("postId");
    }
    this.postId = postId;
    return this;
  }

  public Date date() {
    return date;
  }

  public CommentBuilder date(Date date) {
    if (date == null) {
      throw new NullPointerException("date");
    }
    this.date = date;
    return this;
  }

  public Integer likes() {
    return likes;
  }

  public CommentBuilder likes(@Nullable Integer likes) {
    this.likes = likes;
    return this;
  }

  public Comment build() {
    return new Value(id, writerId, postId, date, likes);
  }

  public static CommentBuilder from(Comment v) {
    return new CommentBuilder(v);
  }

  public static CommentBuilder from(CommentBuilder v) {
    return new CommentBuilder(v);
  }

  private static final class Value implements Comment {
    private final String id;

    private final String writerId;

    private final String postId;

    private final Date date;

    private final Integer likes;

    private Value(@AutoMatter.Field("id") String id, @AutoMatter.Field("writerId") String writerId,
        @AutoMatter.Field("postId") String postId, @AutoMatter.Field("date") Date date,
        @AutoMatter.Field("likes") Integer likes) {
      if (id == null) {
        throw new NullPointerException("id");
      }
      if (writerId == null) {
        throw new NullPointerException("writerId");
      }
      if (postId == null) {
        throw new NullPointerException("postId");
      }
      if (date == null) {
        throw new NullPointerException("date");
      }
      this.id = id;
      this.writerId = writerId;
      this.postId = postId;
      this.date = date;
      this.likes = likes;
    }

    @AutoMatter.Field
    @Override
    public String id() {
      return id;
    }

    @AutoMatter.Field
    @Override
    public String writerId() {
      return writerId;
    }

    @AutoMatter.Field
    @Override
    public String postId() {
      return postId;
    }

    @AutoMatter.Field
    @Override
    public Date date() {
      return date;
    }

    @AutoMatter.Field
    @Override
    public Integer likes() {
      return likes;
    }

    public CommentBuilder builder() {
      return new CommentBuilder(this);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof Comment)) {
        return false;
      }
      final Comment that = (Comment) o;
      if (id != null ? !id.equals(that.id()) : that.id() != null) {
        return false;
      }
      if (writerId != null ? !writerId.equals(that.writerId()) : that.writerId() != null) {
        return false;
      }
      if (postId != null ? !postId.equals(that.postId()) : that.postId() != null) {
        return false;
      }
      if (date != null ? !date.equals(that.date()) : that.date() != null) {
        return false;
      }
      if (likes != null ? !likes.equals(that.likes()) : that.likes() != null) {
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
      result = 31 * result + (this.postId != null ? this.postId.hashCode() : 0);
      result = 31 * result + (this.date != null ? this.date.hashCode() : 0);
      result = 31 * result + (this.likes != null ? this.likes.hashCode() : 0);
      return result;
    }

    @Override
    public String toString() {
      return "Comment{" +
      "id=" + id +
      ", writerId=" + writerId +
      ", postId=" + postId +
      ", date=" + date +
      ", likes=" + likes +
      '}';
    }
  }
}
