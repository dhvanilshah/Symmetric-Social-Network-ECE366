package cool.disc.server.model;

import io.norberg.automatter.AutoMatter;
import java.util.Date;
import javax.annotation.Generated;

@Generated("io.norberg.automatter.processor.AutoMatterProcessor")
public final class RequestBuilder {
  private String userId;

  private Date date;

  public RequestBuilder() {
  }

  private RequestBuilder(Request v) {
    this.userId = v.userId();
    this.date = v.date();
  }

  private RequestBuilder(RequestBuilder v) {
    this.userId = v.userId;
    this.date = v.date;
  }

  public String userId() {
    return userId;
  }

  public RequestBuilder userId(String userId) {
    if (userId == null) {
      throw new NullPointerException("userId");
    }
    this.userId = userId;
    return this;
  }

  public Date date() {
    return date;
  }

  public RequestBuilder date(Date date) {
    if (date == null) {
      throw new NullPointerException("date");
    }
    this.date = date;
    return this;
  }

  public Request build() {
    return new Value(userId, date);
  }

  public static RequestBuilder from(Request v) {
    return new RequestBuilder(v);
  }

  public static RequestBuilder from(RequestBuilder v) {
    return new RequestBuilder(v);
  }

  private static final class Value implements Request {
    private final String userId;

    private final Date date;

    private Value(@AutoMatter.Field("userId") String userId, @AutoMatter.Field("date") Date date) {
      if (userId == null) {
        throw new NullPointerException("userId");
      }
      if (date == null) {
        throw new NullPointerException("date");
      }
      this.userId = userId;
      this.date = date;
    }

    @AutoMatter.Field
    @Override
    public String userId() {
      return userId;
    }

    @AutoMatter.Field
    @Override
    public Date date() {
      return date;
    }

    public RequestBuilder builder() {
      return new RequestBuilder(this);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof Request)) {
        return false;
      }
      final Request that = (Request) o;
      if (userId != null ? !userId.equals(that.userId()) : that.userId() != null) {
        return false;
      }
      if (date != null ? !date.equals(that.date()) : that.date() != null) {
        return false;
      }
      return true;
    }

    @Override
    public int hashCode() {
      int result = 1;
      long temp;
      result = 31 * result + (this.userId != null ? this.userId.hashCode() : 0);
      result = 31 * result + (this.date != null ? this.date.hashCode() : 0);
      return result;
    }

    @Override
    public String toString() {
      return "Request{" +
      "userId=" + userId +
      ", date=" + date +
      '}';
    }
  }
}
