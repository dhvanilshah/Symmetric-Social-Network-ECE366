package cool.disc.server.model;

import io.norberg.automatter.AutoMatter;

import javax.annotation.Nullable;

@AutoMatter
public interface Song {
    String id();
    String artist();
    @Nullable String album();
    @Nullable Integer score();
    String title();
    @Nullable String url();
}
