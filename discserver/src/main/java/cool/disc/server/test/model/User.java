package cool.disc.server.test.model;

import io.norberg.automatter.AutoMatter;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.List;

@AutoMatter
public interface User {
    @Nullable String id();
    @Nullable String name();
    @Nullable String username();
    @Nullable String password();
    @Nullable String email();
    @Nullable String birthday();
    @Nullable String photo();
    @Nullable String bio();
    @Nullable String faveSong();
    @Nullable Date dateCreated();
    @Nullable List<Friend> friends();
    @Nullable List<Request> reqSent();
    @Nullable List<Request> reqReceived();
    @Nullable List<String> likedPosts();
    @Nullable List<String> likedComments();
    @Nullable Boolean friendCheck();
}
