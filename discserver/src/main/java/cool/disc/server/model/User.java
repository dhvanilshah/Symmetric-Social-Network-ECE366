package cool.disc.server.model;

import io.norberg.automatter.AutoMatter;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.List;

@AutoMatter
public interface User {
    String id();
    String name();
    String username();
    @Nullable String password();
    @Nullable String email();
    @Nullable String service();
    @Nullable String photo();
    @Nullable Date dateCreated();
    @Nullable List<Friend> friends();
    @Nullable List<Request> reqSent();
    @Nullable List<Request> reqReceived();
    @Nullable List<String> likedPosts();
    @Nullable List<String> likedComments();


}