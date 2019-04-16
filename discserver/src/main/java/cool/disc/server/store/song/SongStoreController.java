package cool.disc.server.store.song;

import org.bson.types.ObjectId;

import javax.annotation.Nullable;

public class SongStoreController implements SongStore {

    @Override
    public ObjectId getSongId() {
        return null;
    }

    @Override
    public String artist() {
        return null;
    }

    @Nullable
    @Override
    public String album() {
        return null;
    }

    @Nullable
    @Override
    public Integer score() {
        return null;
    }

    @Override
    public String title() {
        return null;
    }

    @Nullable
    @Override
    public String url() {
        return null;
    }
}
