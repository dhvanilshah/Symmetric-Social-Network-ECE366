package cool.disc.server.store.song;

import com.spotify.apollo.Response;
import cool.disc.server.model.Song;

public interface SongStore {
    Response<Object> addSong(Song song);

}
