package cool.disc.server.model;

import io.norberg.automatter.AutoMatter;
import org.bson.types.ObjectId;

import javax.annotation.Nullable;

@AutoMatter
public interface Song {
    @Nullable ObjectId id();
    @Nullable String songId();
    @Nullable String title();
    @Nullable String songUrl();
    @Nullable String artist();
    @Nullable String artistId();
    @Nullable String albumName();
    @Nullable String albumImageUrl();
    @Nullable Integer score();
}
