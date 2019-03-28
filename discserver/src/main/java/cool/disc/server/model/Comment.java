package cool.disc.server.model;

import io.norberg.automatter.AutoMatter;

import javax.annotation.Nullable;
import java.util.Date;

@AutoMatter
public interface Comment {
    String id();
    String writerId();
    String postId();
    Date date();
    @Nullable Integer likes();
}
