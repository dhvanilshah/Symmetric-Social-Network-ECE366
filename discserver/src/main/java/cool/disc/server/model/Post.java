package cool.disc.server.model;

import io.norberg.automatter.AutoMatter;

import javax.annotation.Nullable;

@AutoMatter
public interface Post {
    String id();
    String receiverId();
    String writerId();
    @Nullable String message();
    @Nullable Integer isPrivate();
}