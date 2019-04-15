package cool.disc.server.store.comment;

import cool.disc.server.model.Comment;
import io.norberg.automatter.AutoMatter;
import java.util.Date;
import javax.annotation.Generated;
import org.bson.types.ObjectId;

@Generated("io.norberg.automatter.processor.AutoMatterProcessor")
public final class CommentStoreBuilder {
  private Comment addComment;

  private Comment deleteComment;

  private ObjectId getId;

  private ObjectId getWriterId;

  private ObjectId getPostId;

  private Date getDate;

  private Integer getLikes;

  public CommentStoreBuilder() {
  }

  private CommentStoreBuilder(CommentStore v) {
    this.addComment = v.addComment();
    this.deleteComment = v.deleteComment();
    this.getId = v.getId();
    this.getWriterId = v.getWriterId();
    this.getPostId = v.getPostId();
    this.getDate = v.getDate();
    this.getLikes = v.getLikes();
  }

  private CommentStoreBuilder(CommentStoreBuilder v) {
    this.addComment = v.addComment;
    this.deleteComment = v.deleteComment;
    this.getId = v.getId;
    this.getWriterId = v.getWriterId;
    this.getPostId = v.getPostId;
    this.getDate = v.getDate;
    this.getLikes = v.getLikes;
  }

  public Comment addComment() {
    return addComment;
  }

  public CommentStoreBuilder addComment(Comment addComment) {
    if (addComment == null) {
      throw new NullPointerException("addComment");
    }
    this.addComment = addComment;
    return this;
  }

  public Comment deleteComment() {
    return deleteComment;
  }

  public CommentStoreBuilder deleteComment(Comment deleteComment) {
    if (deleteComment == null) {
      throw new NullPointerException("deleteComment");
    }
    this.deleteComment = deleteComment;
    return this;
  }

  public ObjectId getId() {
    return getId;
  }

  public CommentStoreBuilder getId(ObjectId getId) {
    if (getId == null) {
      throw new NullPointerException("getId");
    }
    this.getId = getId;
    return this;
  }

  public ObjectId getWriterId() {
    return getWriterId;
  }

  public CommentStoreBuilder getWriterId(ObjectId getWriterId) {
    if (getWriterId == null) {
      throw new NullPointerException("getWriterId");
    }
    this.getWriterId = getWriterId;
    return this;
  }

  public ObjectId getPostId() {
    return getPostId;
  }

  public CommentStoreBuilder getPostId(ObjectId getPostId) {
    if (getPostId == null) {
      throw new NullPointerException("getPostId");
    }
    this.getPostId = getPostId;
    return this;
  }

  public Date getDate() {
    return getDate;
  }

  public CommentStoreBuilder getDate(Date getDate) {
    if (getDate == null) {
      throw new NullPointerException("getDate");
    }
    this.getDate = getDate;
    return this;
  }

  public Integer getLikes() {
    return getLikes;
  }

  public CommentStoreBuilder getLikes(Integer getLikes) {
    if (getLikes == null) {
      throw new NullPointerException("getLikes");
    }
    this.getLikes = getLikes;
    return this;
  }

  public CommentStore build() {
    return new Value(addComment, deleteComment, getId, getWriterId, getPostId, getDate, getLikes);
  }

  public static CommentStoreBuilder from(CommentStore v) {
    return new CommentStoreBuilder(v);
  }

  public static CommentStoreBuilder from(CommentStoreBuilder v) {
    return new CommentStoreBuilder(v);
  }

  private static final class Value implements CommentStore {
    private final Comment addComment;

    private final Comment deleteComment;

    private final ObjectId getId;

    private final ObjectId getWriterId;

    private final ObjectId getPostId;

    private final Date getDate;

    private final Integer getLikes;

    private Value(@AutoMatter.Field("addComment") Comment addComment,
        @AutoMatter.Field("deleteComment") Comment deleteComment,
        @AutoMatter.Field("getId") ObjectId getId,
        @AutoMatter.Field("getWriterId") ObjectId getWriterId,
        @AutoMatter.Field("getPostId") ObjectId getPostId,
        @AutoMatter.Field("getDate") Date getDate, @AutoMatter.Field("getLikes") Integer getLikes) {
      if (addComment == null) {
        throw new NullPointerException("addComment");
      }
      if (deleteComment == null) {
        throw new NullPointerException("deleteComment");
      }
      if (getId == null) {
        throw new NullPointerException("getId");
      }
      if (getWriterId == null) {
        throw new NullPointerException("getWriterId");
      }
      if (getPostId == null) {
        throw new NullPointerException("getPostId");
      }
      if (getDate == null) {
        throw new NullPointerException("getDate");
      }
      if (getLikes == null) {
        throw new NullPointerException("getLikes");
      }
      this.addComment = addComment;
      this.deleteComment = deleteComment;
      this.getId = getId;
      this.getWriterId = getWriterId;
      this.getPostId = getPostId;
      this.getDate = getDate;
      this.getLikes = getLikes;
    }

    @AutoMatter.Field
    @Override
    public Comment addComment() {
      return addComment;
    }

    @AutoMatter.Field
    @Override
    public Comment deleteComment() {
      return deleteComment;
    }

    @AutoMatter.Field
    @Override
    public ObjectId getId() {
      return getId;
    }

    @AutoMatter.Field
    @Override
    public ObjectId getWriterId() {
      return getWriterId;
    }

    @AutoMatter.Field
    @Override
    public ObjectId getPostId() {
      return getPostId;
    }

    @AutoMatter.Field
    @Override
    public Date getDate() {
      return getDate;
    }

    @AutoMatter.Field
    @Override
    public Integer getLikes() {
      return getLikes;
    }

    public CommentStoreBuilder builder() {
      return new CommentStoreBuilder(this);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof CommentStore)) {
        return false;
      }
      final CommentStore that = (CommentStore) o;
      if (addComment != null ? !addComment.equals(that.addComment()) : that.addComment() != null) {
        return false;
      }
      if (deleteComment != null ? !deleteComment.equals(that.deleteComment()) : that.deleteComment() != null) {
        return false;
      }
      if (getId != null ? !getId.equals(that.getId()) : that.getId() != null) {
        return false;
      }
      if (getWriterId != null ? !getWriterId.equals(that.getWriterId()) : that.getWriterId() != null) {
        return false;
      }
      if (getPostId != null ? !getPostId.equals(that.getPostId()) : that.getPostId() != null) {
        return false;
      }
      if (getDate != null ? !getDate.equals(that.getDate()) : that.getDate() != null) {
        return false;
      }
      if (getLikes != null ? !getLikes.equals(that.getLikes()) : that.getLikes() != null) {
        return false;
      }
      return true;
    }

    @Override
    public int hashCode() {
      int result = 1;
      long temp;
      result = 31 * result + (this.addComment != null ? this.addComment.hashCode() : 0);
      result = 31 * result + (this.deleteComment != null ? this.deleteComment.hashCode() : 0);
      result = 31 * result + (this.getId != null ? this.getId.hashCode() : 0);
      result = 31 * result + (this.getWriterId != null ? this.getWriterId.hashCode() : 0);
      result = 31 * result + (this.getPostId != null ? this.getPostId.hashCode() : 0);
      result = 31 * result + (this.getDate != null ? this.getDate.hashCode() : 0);
      result = 31 * result + (this.getLikes != null ? this.getLikes.hashCode() : 0);
      return result;
    }

    @Override
    public String toString() {
      return "CommentStore{" +
      "addComment=" + addComment +
      ", deleteComment=" + deleteComment +
      ", getId=" + getId +
      ", getWriterId=" + getWriterId +
      ", getPostId=" + getPostId +
      ", getDate=" + getDate +
      ", getLikes=" + getLikes +
      '}';
    }
  }
}
