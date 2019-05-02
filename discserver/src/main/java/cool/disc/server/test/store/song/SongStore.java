package cool.disc.server.test.store.song;

import com.spotify.apollo.Response;
import cool.disc.server.test.model.Song;

public interface SongStore {
    Response<Object> addSong(Song song);

}
