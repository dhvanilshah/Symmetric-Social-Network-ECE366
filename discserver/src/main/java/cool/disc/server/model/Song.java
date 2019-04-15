package cool.disc.server.model;

import io.norberg.automatter.AutoMatter;
import org.bson.types.ObjectId;

import javax.annotation.Nullable;

@AutoMatter
public interface Song {
    ObjectId id();
    String artist();
    @Nullable String album();
    @Nullable Integer score();
    String title();
    @Nullable String url();
}
