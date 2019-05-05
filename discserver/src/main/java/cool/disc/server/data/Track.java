package cool.disc.server.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.exceptions.UnirestException;
import cool.disc.server.handler.auth.Authenticate;
import cool.disc.server.model.Song;
import cool.disc.server.model.SongBuilder;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Track {
    private static final Logger LOG = LoggerFactory.getLogger(Track.class);

    private final ObjectMapper objectMapper;
    JSONObject result;
    Authenticate auth;
    String trackTitle;
    String trackType;

    public Track(ObjectMapper objectMapper, String trackTitle, String trackType) throws IOException, UnirestException {
        this.objectMapper = objectMapper;
        this.auth = new Authenticate(this.objectMapper);
        this.trackTitle = trackTitle;
        this.trackType = trackType;
    }

    public String getTrackType() { return this.trackType; }
    public String getTrackTitle() { return this.trackTitle; }

    private JSONObject getTracks() {
        try {
            Authenticate auth = null;
                auth = new Authenticate(objectMapper);
                result = auth.search(this.trackTitle, this.trackType);
        } catch (UnirestException | IOException e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    public List<Song> searchSongs() {
        List<Song> songs = new ArrayList<>();
        result = getTracks();
        try {
            JSONArray items = result.getJSONObject("tracks").getJSONArray("items");
            songs = makeSongs(songs, items);
        } catch (NullPointerException e) {
            LOG.error(e.getMessage());
        }
        return songs;
    }

    // recommendation based on current query search
    public JSONObject getRecommendations(String title, String genre) {
        try {
            Authenticate auth = null;
            auth = new Authenticate(objectMapper);
            result = auth.searchRecommendations(title, genre);
            return result;
        } catch (UnirestException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Song> searchRecommendations(String query, String genre) {
        List<Song> songs = new ArrayList<>();
        JSONObject result = getRecommendations(query, genre);
        try {
            JSONArray tracks = result.getJSONArray("tracks");
            songs = makeSongs(songs, tracks);
        } catch (NullPointerException e) {
            LOG.error(e.getMessage());
        }
        return songs;
    }

    // utility function for returning Songs from an array of JSONArray
    private List<Song> makeSongs(List<Song> songs, JSONArray items) {
        Song searchedSong;
        for (int i = 0; i < items.length(); i++) {
            String artist = items.getJSONObject(i).getJSONObject("album").getJSONArray("artists").getJSONObject(0).getString("name");
            String artistId = items.getJSONObject(i).getJSONObject("album").getJSONArray("artists").getJSONObject(0 ).getString("id");
            String songId = items.getJSONObject(i).getString("id");
            String album = items.getJSONObject(i).getJSONObject("album").getString("name");
            String url = items.getJSONObject(i).getJSONObject("external_urls").getString("spotify");
            String name = items.getJSONObject(i).getString("name");
            // index 0: width and height 640px, 1: 300px, 2: 64px
            String albumImageUrl = items.getJSONObject(i).getJSONObject("album").getJSONArray("images").getJSONObject(0).getString("url");
            searchedSong = new SongBuilder()
                    .id(new ObjectId())
                    .songId(songId)
                    .title(name)
                    .songUrl(url)
                    .artist(artist)
                    .artistId(artistId)
                    .albumName(album)
                    .albumImageUrl(albumImageUrl)
                    .score(0)
                    .build();
            songs.add(searchedSong);
        }
        return songs;
    }

}
