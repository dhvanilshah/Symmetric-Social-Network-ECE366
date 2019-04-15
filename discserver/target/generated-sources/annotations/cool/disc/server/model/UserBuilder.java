package cool.disc.server.model;

import io.norberg.automatter.AutoMatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Generated;
import javax.annotation.Nullable;

@Generated("io.norberg.automatter.processor.AutoMatterProcessor")
public final class UserBuilder {
  private String id;

  private String name;

  private String username;

  private String password;

  private String email;

  private String service;

  private String photo;

  private Date dateCreated;

  private List<Friend> friends;

  private List<Request> reqSent;

  private List<Request> reqReceived;

  private List<String> likedPosts;

  private List<String> likedComments;

  public UserBuilder() {
  }

  private UserBuilder(User v) {
    this.id = v.id();
    this.name = v.name();
    this.username = v.username();
    this.password = v.password();
    this.email = v.email();
    this.service = v.service();
    this.photo = v.photo();
    this.dateCreated = v.dateCreated();
    List<Friend> _friends = v.friends();
    this.friends = (_friends == null) ? null : new ArrayList<Friend>(_friends);
    List<Request> _reqSent = v.reqSent();
    this.reqSent = (_reqSent == null) ? null : new ArrayList<Request>(_reqSent);
    List<Request> _reqReceived = v.reqReceived();
    this.reqReceived = (_reqReceived == null) ? null : new ArrayList<Request>(_reqReceived);
    List<String> _likedPosts = v.likedPosts();
    this.likedPosts = (_likedPosts == null) ? null : new ArrayList<String>(_likedPosts);
    List<String> _likedComments = v.likedComments();
    this.likedComments = (_likedComments == null) ? null : new ArrayList<String>(_likedComments);
  }

  private UserBuilder(UserBuilder v) {
    this.id = v.id;
    this.name = v.name;
    this.username = v.username;
    this.password = v.password;
    this.email = v.email;
    this.service = v.service;
    this.photo = v.photo;
    this.dateCreated = v.dateCreated;
    this.friends = (v.friends == null) ? null : new ArrayList<Friend>(v.friends);
    this.reqSent = (v.reqSent == null) ? null : new ArrayList<Request>(v.reqSent);
    this.reqReceived = (v.reqReceived == null) ? null : new ArrayList<Request>(v.reqReceived);
    this.likedPosts = (v.likedPosts == null) ? null : new ArrayList<String>(v.likedPosts);
    this.likedComments = (v.likedComments == null) ? null : new ArrayList<String>(v.likedComments);
  }

  public String id() {
    return id;
  }

  public UserBuilder id(String id) {
    if (id == null) {
      throw new NullPointerException("id");
    }
    this.id = id;
    return this;
  }

  public String name() {
    return name;
  }

  public UserBuilder name(String name) {
    if (name == null) {
      throw new NullPointerException("name");
    }
    this.name = name;
    return this;
  }

  public String username() {
    return username;
  }

  public UserBuilder username(String username) {
    if (username == null) {
      throw new NullPointerException("username");
    }
    this.username = username;
    return this;
  }

  public String password() {
    return password;
  }

  public UserBuilder password(@Nullable String password) {
    this.password = password;
    return this;
  }

  public String email() {
    return email;
  }

  public UserBuilder email(@Nullable String email) {
    this.email = email;
    return this;
  }

  public String service() {
    return service;
  }

  public UserBuilder service(@Nullable String service) {
    this.service = service;
    return this;
  }

  public String photo() {
    return photo;
  }

  public UserBuilder photo(@Nullable String photo) {
    this.photo = photo;
    return this;
  }

  public Date dateCreated() {
    return dateCreated;
  }

  public UserBuilder dateCreated(@Nullable Date dateCreated) {
    this.dateCreated = dateCreated;
    return this;
  }

  public List<Friend> friends() {
    return friends;
  }

  public UserBuilder friends(List<? extends Friend> friends) {
    return friends((Collection<? extends Friend>) friends);
  }

  public UserBuilder friends(Collection<? extends Friend> friends) {
    if (friends == null) {
      this.friends = null;
      return this;
    }
    this.friends = new ArrayList<Friend>(friends);
    return this;
  }

  public UserBuilder friends(Iterable<? extends Friend> friends) {
    if (friends == null) {
      this.friends = null;
      return this;
    }
    if (friends instanceof Collection) {
      return friends((Collection<? extends Friend>) friends);
    }
    return friends(friends.iterator());
  }

  public UserBuilder friends(Iterator<? extends Friend> friends) {
    if (friends == null) {
      this.friends = null;
      return this;
    }
    this.friends = new ArrayList<Friend>();
    while (friends.hasNext()) {
      Friend item = friends.next();
      this.friends.add(item);
    }
    return this;
  }

  @SafeVarargs
  public final UserBuilder friends(Friend... friends) {
    if (friends == null) {
      this.friends = null;
      return this;
    }
    return friends(Arrays.asList(friends));
  }

  public UserBuilder addFriend(Friend friend) {
    if (this.friends == null) {
      this.friends = new ArrayList<Friend>();
    }
    friends.add(friend);
    return this;
  }

  public List<Request> reqSent() {
    return reqSent;
  }

  public UserBuilder reqSent(List<? extends Request> reqSent) {
    return reqSent((Collection<? extends Request>) reqSent);
  }

  public UserBuilder reqSent(Collection<? extends Request> reqSent) {
    if (reqSent == null) {
      this.reqSent = null;
      return this;
    }
    this.reqSent = new ArrayList<Request>(reqSent);
    return this;
  }

  public UserBuilder reqSent(Iterable<? extends Request> reqSent) {
    if (reqSent == null) {
      this.reqSent = null;
      return this;
    }
    if (reqSent instanceof Collection) {
      return reqSent((Collection<? extends Request>) reqSent);
    }
    return reqSent(reqSent.iterator());
  }

  public UserBuilder reqSent(Iterator<? extends Request> reqSent) {
    if (reqSent == null) {
      this.reqSent = null;
      return this;
    }
    this.reqSent = new ArrayList<Request>();
    while (reqSent.hasNext()) {
      Request item = reqSent.next();
      this.reqSent.add(item);
    }
    return this;
  }

  @SafeVarargs
  public final UserBuilder reqSent(Request... reqSent) {
    if (reqSent == null) {
      this.reqSent = null;
      return this;
    }
    return reqSent(Arrays.asList(reqSent));
  }

  public List<Request> reqReceived() {
    return reqReceived;
  }

  public UserBuilder reqReceived(List<? extends Request> reqReceived) {
    return reqReceived((Collection<? extends Request>) reqReceived);
  }

  public UserBuilder reqReceived(Collection<? extends Request> reqReceived) {
    if (reqReceived == null) {
      this.reqReceived = null;
      return this;
    }
    this.reqReceived = new ArrayList<Request>(reqReceived);
    return this;
  }

  public UserBuilder reqReceived(Iterable<? extends Request> reqReceived) {
    if (reqReceived == null) {
      this.reqReceived = null;
      return this;
    }
    if (reqReceived instanceof Collection) {
      return reqReceived((Collection<? extends Request>) reqReceived);
    }
    return reqReceived(reqReceived.iterator());
  }

  public UserBuilder reqReceived(Iterator<? extends Request> reqReceived) {
    if (reqReceived == null) {
      this.reqReceived = null;
      return this;
    }
    this.reqReceived = new ArrayList<Request>();
    while (reqReceived.hasNext()) {
      Request item = reqReceived.next();
      this.reqReceived.add(item);
    }
    return this;
  }

  @SafeVarargs
  public final UserBuilder reqReceived(Request... reqReceived) {
    if (reqReceived == null) {
      this.reqReceived = null;
      return this;
    }
    return reqReceived(Arrays.asList(reqReceived));
  }

  public List<String> likedPosts() {
    return likedPosts;
  }

  public UserBuilder likedPosts(List<? extends String> likedPosts) {
    return likedPosts((Collection<? extends String>) likedPosts);
  }

  public UserBuilder likedPosts(Collection<? extends String> likedPosts) {
    if (likedPosts == null) {
      this.likedPosts = null;
      return this;
    }
    this.likedPosts = new ArrayList<String>(likedPosts);
    return this;
  }

  public UserBuilder likedPosts(Iterable<? extends String> likedPosts) {
    if (likedPosts == null) {
      this.likedPosts = null;
      return this;
    }
    if (likedPosts instanceof Collection) {
      return likedPosts((Collection<? extends String>) likedPosts);
    }
    return likedPosts(likedPosts.iterator());
  }

  public UserBuilder likedPosts(Iterator<? extends String> likedPosts) {
    if (likedPosts == null) {
      this.likedPosts = null;
      return this;
    }
    this.likedPosts = new ArrayList<String>();
    while (likedPosts.hasNext()) {
      String item = likedPosts.next();
      this.likedPosts.add(item);
    }
    return this;
  }

  @SafeVarargs
  public final UserBuilder likedPosts(String... likedPosts) {
    if (likedPosts == null) {
      this.likedPosts = null;
      return this;
    }
    return likedPosts(Arrays.asList(likedPosts));
  }

  public UserBuilder addLikedPost(String likedPost) {
    if (this.likedPosts == null) {
      this.likedPosts = new ArrayList<String>();
    }
    likedPosts.add(likedPost);
    return this;
  }

  public List<String> likedComments() {
    return likedComments;
  }

  public UserBuilder likedComments(List<? extends String> likedComments) {
    return likedComments((Collection<? extends String>) likedComments);
  }

  public UserBuilder likedComments(Collection<? extends String> likedComments) {
    if (likedComments == null) {
      this.likedComments = null;
      return this;
    }
    this.likedComments = new ArrayList<String>(likedComments);
    return this;
  }

  public UserBuilder likedComments(Iterable<? extends String> likedComments) {
    if (likedComments == null) {
      this.likedComments = null;
      return this;
    }
    if (likedComments instanceof Collection) {
      return likedComments((Collection<? extends String>) likedComments);
    }
    return likedComments(likedComments.iterator());
  }

  public UserBuilder likedComments(Iterator<? extends String> likedComments) {
    if (likedComments == null) {
      this.likedComments = null;
      return this;
    }
    this.likedComments = new ArrayList<String>();
    while (likedComments.hasNext()) {
      String item = likedComments.next();
      this.likedComments.add(item);
    }
    return this;
  }

  @SafeVarargs
  public final UserBuilder likedComments(String... likedComments) {
    if (likedComments == null) {
      this.likedComments = null;
      return this;
    }
    return likedComments(Arrays.asList(likedComments));
  }

  public UserBuilder addLikedComment(String likedComment) {
    if (this.likedComments == null) {
      this.likedComments = new ArrayList<String>();
    }
    likedComments.add(likedComment);
    return this;
  }

  public User build() {
    List<Friend> _friends = (friends != null) ? Collections.unmodifiableList(new ArrayList<Friend>(friends)) : null;
    List<Request> _reqSent = (reqSent != null) ? Collections.unmodifiableList(new ArrayList<Request>(reqSent)) : null;
    List<Request> _reqReceived = (reqReceived != null) ? Collections.unmodifiableList(new ArrayList<Request>(reqReceived)) : null;
    List<String> _likedPosts = (likedPosts != null) ? Collections.unmodifiableList(new ArrayList<String>(likedPosts)) : null;
    List<String> _likedComments = (likedComments != null) ? Collections.unmodifiableList(new ArrayList<String>(likedComments)) : null;
    return new Value(id, name, username, password, email, service, photo, dateCreated, _friends, _reqSent, _reqReceived, _likedPosts, _likedComments);
  }

  public static UserBuilder from(User v) {
    return new UserBuilder(v);
  }

  public static UserBuilder from(UserBuilder v) {
    return new UserBuilder(v);
  }

  private static final class Value implements User {
    private final String id;

    private final String name;

    private final String username;

    private final String password;

    private final String email;

    private final String service;

    private final String photo;

    private final Date dateCreated;

    private final List<Friend> friends;

    private final List<Request> reqSent;

    private final List<Request> reqReceived;

    private final List<String> likedPosts;

    private final List<String> likedComments;

    private Value(@AutoMatter.Field("id") String id, @AutoMatter.Field("name") String name,
        @AutoMatter.Field("username") String username,
        @AutoMatter.Field("password") String password, @AutoMatter.Field("email") String email,
        @AutoMatter.Field("service") String service, @AutoMatter.Field("photo") String photo,
        @AutoMatter.Field("dateCreated") Date dateCreated,
        @AutoMatter.Field("friends") List<Friend> friends,
        @AutoMatter.Field("reqSent") List<Request> reqSent,
        @AutoMatter.Field("reqReceived") List<Request> reqReceived,
        @AutoMatter.Field("likedPosts") List<String> likedPosts,
        @AutoMatter.Field("likedComments") List<String> likedComments) {
      if (id == null) {
        throw new NullPointerException("id");
      }
      if (name == null) {
        throw new NullPointerException("name");
      }
      if (username == null) {
        throw new NullPointerException("username");
      }
      this.id = id;
      this.name = name;
      this.username = username;
      this.password = password;
      this.email = email;
      this.service = service;
      this.photo = photo;
      this.dateCreated = dateCreated;
      this.friends = friends;
      this.reqSent = reqSent;
      this.reqReceived = reqReceived;
      this.likedPosts = likedPosts;
      this.likedComments = likedComments;
    }

    @AutoMatter.Field
    @Override
    public String id() {
      return id;
    }

    @AutoMatter.Field
    @Override
    public String name() {
      return name;
    }

    @AutoMatter.Field
    @Override
    public String username() {
      return username;
    }

    @AutoMatter.Field
    @Override
    public String password() {
      return password;
    }

    @AutoMatter.Field
    @Override
    public String email() {
      return email;
    }

    @AutoMatter.Field
    @Override
    public String service() {
      return service;
    }

    @AutoMatter.Field
    @Override
    public String photo() {
      return photo;
    }

    @AutoMatter.Field
    @Override
    public Date dateCreated() {
      return dateCreated;
    }

    @AutoMatter.Field
    @Override
    public List<Friend> friends() {
      return friends;
    }

    @AutoMatter.Field
    @Override
    public List<Request> reqSent() {
      return reqSent;
    }

    @AutoMatter.Field
    @Override
    public List<Request> reqReceived() {
      return reqReceived;
    }

    @AutoMatter.Field
    @Override
    public List<String> likedPosts() {
      return likedPosts;
    }

    @AutoMatter.Field
    @Override
    public List<String> likedComments() {
      return likedComments;
    }

    public UserBuilder builder() {
      return new UserBuilder(this);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof User)) {
        return false;
      }
      final User that = (User) o;
      if (id != null ? !id.equals(that.id()) : that.id() != null) {
        return false;
      }
      if (name != null ? !name.equals(that.name()) : that.name() != null) {
        return false;
      }
      if (username != null ? !username.equals(that.username()) : that.username() != null) {
        return false;
      }
      if (password != null ? !password.equals(that.password()) : that.password() != null) {
        return false;
      }
      if (email != null ? !email.equals(that.email()) : that.email() != null) {
        return false;
      }
      if (service != null ? !service.equals(that.service()) : that.service() != null) {
        return false;
      }
      if (photo != null ? !photo.equals(that.photo()) : that.photo() != null) {
        return false;
      }
      if (dateCreated != null ? !dateCreated.equals(that.dateCreated()) : that.dateCreated() != null) {
        return false;
      }
      if (friends != null ? !friends.equals(that.friends()) : that.friends() != null) {
        return false;
      }
      if (reqSent != null ? !reqSent.equals(that.reqSent()) : that.reqSent() != null) {
        return false;
      }
      if (reqReceived != null ? !reqReceived.equals(that.reqReceived()) : that.reqReceived() != null) {
        return false;
      }
      if (likedPosts != null ? !likedPosts.equals(that.likedPosts()) : that.likedPosts() != null) {
        return false;
      }
      if (likedComments != null ? !likedComments.equals(that.likedComments()) : that.likedComments() != null) {
        return false;
      }
      return true;
    }

    @Override
    public int hashCode() {
      int result = 1;
      long temp;
      result = 31 * result + (this.id != null ? this.id.hashCode() : 0);
      result = 31 * result + (this.name != null ? this.name.hashCode() : 0);
      result = 31 * result + (this.username != null ? this.username.hashCode() : 0);
      result = 31 * result + (this.password != null ? this.password.hashCode() : 0);
      result = 31 * result + (this.email != null ? this.email.hashCode() : 0);
      result = 31 * result + (this.service != null ? this.service.hashCode() : 0);
      result = 31 * result + (this.photo != null ? this.photo.hashCode() : 0);
      result = 31 * result + (this.dateCreated != null ? this.dateCreated.hashCode() : 0);
      result = 31 * result + (this.friends != null ? this.friends.hashCode() : 0);
      result = 31 * result + (this.reqSent != null ? this.reqSent.hashCode() : 0);
      result = 31 * result + (this.reqReceived != null ? this.reqReceived.hashCode() : 0);
      result = 31 * result + (this.likedPosts != null ? this.likedPosts.hashCode() : 0);
      result = 31 * result + (this.likedComments != null ? this.likedComments.hashCode() : 0);
      return result;
    }

    @Override
    public String toString() {
      return "User{" +
      "id=" + id +
      ", name=" + name +
      ", username=" + username +
      ", password=" + password +
      ", email=" + email +
      ", service=" + service +
      ", photo=" + photo +
      ", dateCreated=" + dateCreated +
      ", friends=" + friends +
      ", reqSent=" + reqSent +
      ", reqReceived=" + reqReceived +
      ", likedPosts=" + likedPosts +
      ", likedComments=" + likedComments +
      '}';
    }
  }
}
