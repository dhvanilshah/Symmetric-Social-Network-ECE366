package cool.disc.server.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.spotify.apollo.RequestContext;
import cool.disc.server.handler.auth.Authenticate;
import cool.disc.server.model.Song;
import cool.disc.server.model.SongBuilder;
import cool.disc.server.store.song.SongStore;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Track {

    private final ObjectMapper objectMapper;
    JSONObject result;
    Authenticate auth;
    String trackTitle;
    String trackType;
    RequestContext requestContext;
    SongStore songStore;

    public Track(ObjectMapper objectMapper, String trackTitle, String trackType) throws IOException, UnirestException {
        this.objectMapper = objectMapper;
        this.auth = new Authenticate(this.objectMapper);
        this.trackTitle = trackTitle;
        this.trackType = trackType;
    }

    private JSONObject getTrackInfo() {
        try {
            Authenticate auth = null;
                auth = new Authenticate(objectMapper);
                result = auth.search(this.trackTitle, this.trackType);
//                System.out.println("result: " + result);
        } catch (UnirestException | IOException e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    public List<Song> searchSongs() {
//        public List<Map<String, String>> getOpenLinks() {
//        List<String> urls = new ArrayList<>();
        List<Song> songs = new ArrayList<>();
        Song searchedSong;
        result = getTrackInfo();
        try {
            JSONArray items = result.getJSONObject("tracks").getJSONArray("items");
            for (int i = 0 ; i < items.length(); i++) {
                String artist = items.getJSONObject(i).getJSONObject("album").getJSONArray("artists").getJSONObject(0).getString("name").toString();
                String artistId = items.getJSONObject(i).getJSONObject("album").getJSONArray("artists").getJSONObject(0 ).getString("id").toString();
                String songId = items.getJSONObject(i).getJSONObject("album").getString("id");
                String album = items.getJSONObject(i).getJSONObject("album").getString("name");
                String url = items.getJSONObject(i).getJSONObject("album").getJSONObject("external_urls").getString("spotify");
//                urls.add(url);
                // index 0: width and height 640px, 1: 300px, 2: 64px
                String albumImageUrl = items.getJSONObject(i).getJSONObject("album").getJSONArray("images").getJSONObject(0).getString("url");
                searchedSong = new SongBuilder()
                        .id(new ObjectId())
                        .songId(songId)
                        .title(this.trackTitle)
                        .songUrl(url)
                        .artist(artist)
                        .artistId(artistId)
                        .albumName(album)
                        .albumImageUrl(albumImageUrl)
                        .score(0)
                        .build();
                songs.add(searchedSong);

//                System.out.println("Album URL for '"+this.trackTitle+ "': " + url);
            }
        } catch (NullPointerException e) {
            System.err.println(e.getMessage());
        }
//        return urls;
        return songs;
    }




}