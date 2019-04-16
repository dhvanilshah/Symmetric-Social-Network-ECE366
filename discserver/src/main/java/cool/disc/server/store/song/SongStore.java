package cool.disc.server.store.song;

import io.norberg.automatter.AutoMatter;
import org.bson.types.ObjectId;

import javax.annotation.Nullable;

@AutoMatter
public interface SongStore {
    ObjectId getSongId();
    String artist();
    @Nullable
    String album();
    @Nullable Integer score();
    String title();
    @Nullable String url();





}
