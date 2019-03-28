package cool.disc.server.model;

import io.norberg.automatter.AutoMatter;

@AutoMatter
public interface Friend {
    String userId();
    Integer score();
}
