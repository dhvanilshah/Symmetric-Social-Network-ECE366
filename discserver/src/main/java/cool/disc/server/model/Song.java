package cool.disc.server.model;

import io.norberg.automatter.AutoMatter;
import org.bson.types.ObjectId;

@AutoMatter
public interface Song {
    ObjectId id();
    String songId();
    String title();
    String songUrl();
    String artist();
    String artistId();
    String albumName();
    String albumImageUrl();
    Integer score();
}
