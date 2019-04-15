package cool.disc.server.store.request;

import io.norberg.automatter.AutoMatter;
import java.util.Date;
import javax.annotation.Generated;
import org.bson.types.ObjectId;

@Generated("io.norberg.automatter.processor.AutoMatterProcessor")
public final class RequestStoreBuilder {
  private ObjectId getUserId;

  private Date getDate;

  public RequestStoreBuilder() {
  }

  private RequestStoreBuilder(RequestStore v) {
    this.getUserId = v.getUserId();
    this.getDate = v.getDate();
  }

  private RequestStoreBuilder(RequestStoreBuilder v) {
    this.getUserId = v.getUserId;
    this.getDate = v.getDate;
  }

  public ObjectId getUserId() {
    return getUserId;
  }

  public RequestStoreBuilder getUserId(ObjectId getUserId) {
    if (getUserId == null) {
      throw new NullPointerException("getUserId");
    }
    this.getUserId = getUserId;
    return this;
  }

  public Date getDate() {
    return getDate;
  }

  public RequestStoreBuilder getDate(Date getDate) {
    if (getDate == null) {
      throw new NullPointerException("getDate");
    }
    this.getDate = getDate;
    return this;
  }

  public RequestStore build() {
    return new Value(getUserId, getDate);
  }

  public static RequestStoreBuilder from(RequestStore v) {
    return new RequestStoreBuilder(v);
  }

  public static RequestStoreBuilder from(RequestStoreBuilder v) {
    return new RequestStoreBuilder(v);
  }

  private static final class Value implements RequestStore {
    private final ObjectId getUserId;

    private final Date getDate;

    private Value(@AutoMatter.Field("getUserId") ObjectId getUserId,
        @AutoMatter.Field("getDate") Date getDate) {
      if (getUserId == null) {
        throw new NullPointerException("getUserId");
      }
      if (getDate == null) {
        throw new NullPointerException("getDate");
      }
      this.getUserId = getUserId;
      this.getDate = getDate;
    }

    @AutoMatter.Field
    @Override
    public ObjectId getUserId() {
      return getUserId;
    }

    @AutoMatter.Field
    @Override
    public Date getDate() {
      return getDate;
    }

    public RequestStoreBuilder builder() {
      return new RequestStoreBuilder(this);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof RequestStore)) {
        return false;
      }
      final RequestStore that = (RequestStore) o;
      if (getUserId != null ? !getUserId.equals(that.getUserId()) : that.getUserId() != null) {
        return false;
      }
      if (getDate != null ? !getDate.equals(that.getDate()) : that.getDate() != null) {
        return false;
      }
      return true;
    }

    @Override
    public int hashCode() {
      int result = 1;
      long temp;
      result = 31 * result + (this.getUserId != null ? this.getUserId.hashCode() : 0);
      result = 31 * result + (this.getDate != null ? this.getDate.hashCode() : 0);
      return result;
    }

    @Override
    public String toString() {
      return "RequestStore{" +
      "getUserId=" + getUserId +
      ", getDate=" + getDate +
      '}';
    }
  }
}
