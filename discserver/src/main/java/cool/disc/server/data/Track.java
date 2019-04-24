package cool.disc.server.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.exceptions.UnirestException;
import cool.disc.server.handler.auth.Authenticate;
import cool.disc.server.model.Song;
import cool.disc.server.model.SongBuilder;
import javafx.util.Pair;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private JSONObject getTracks() {
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
//        List<String> urls = new ArrayList<>();
        List<Song> songs = new ArrayList<>();
        Song searchedSong;
        result = getTracks();
        try {
            JSONArray items = result.getJSONObject("tracks").getJSONArray("items");
            for (int i = 0 ; i < items.length(); i++) {
                String artist = items.getJSONObject(i).getJSONObject("album").getJSONArray("artists").getJSONObject(0).getString("name");
                String artistId = items.getJSONObject(i).getJSONObject("album").getJSONArray("artists").getJSONObject(0 ).getString("id");
                String songId = items.getJSONObject(i).getJSONObject("album").getString("id");
                String album = items.getJSONObject(i).getJSONObject("album").getString("name");
                String url = items.getJSONObject(i).getJSONObject("album").getJSONObject("external_urls").getString("spotify");
                String name = items.getJSONObject(i).getJSONObject("album").getString("name");
//                urls.add(url);
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
//                System.out.println("Album URL for '"+this.trackTitle+ "': " + url);
            }
        } catch (NullPointerException e) {
            LOG.error(e.getMessage());
        }
//        return urls;
        return songs;
    }

    public JSONObject getRecommendations(String genre) {
        try {
            Authenticate auth = null;
            auth = new Authenticate(objectMapper);
            result = auth.searchRecommendations(genre);
//            LOG.info("results from AUTH: {}",result);
            return result;
        } catch (UnirestException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Pair<String, String> searchRecommendations(String genre) {
        Pair<String, String> results = null;
        JSONObject object = getRecommendations(genre);
        if(object != null) {
            System.out.println(object.getJSONArray("tracks").toString());
        }
        try {
            JSONArray artists = object.getJSONArray("tracks").getJSONObject(0).getJSONArray("artists");
            for (int i = 0 ; i < artists.length(); i++) {
                String title = object.getJSONArray("tracks").getJSONObject(0).getString("name");
                String url = artists.getJSONObject(i).getJSONObject("external_urls").getString("spotify");
//                LOG.info("title: {}, url: {}",title,url);
                results = new Pair<String, String>(title, url);
                return results;
            }
        } catch (NullPointerException e) {
            LOG.error(e.getMessage());
        }
        return null;
    }
    public List<Map<String, String>> getTop(String type) {
        List<Map<String, String>> results = null;


        return results;
    }
}
