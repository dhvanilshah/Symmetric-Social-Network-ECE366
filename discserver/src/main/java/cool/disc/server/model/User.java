package cool.disc.server.model;

import io.norberg.automatter.AutoMatter;

import javax.annotation.Nullable;

@AutoMatter
public interface User {
    String id();
    String username();
    String name();
    @Nullable String password();
    @Nullable String service();
    @Nullable String photo();
}
