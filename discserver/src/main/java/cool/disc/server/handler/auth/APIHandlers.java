package cool.disc.server.handler.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.spotify.apollo.RequestContext;
import com.spotify.apollo.Response;
import com.spotify.apollo.route.*;
import cool.disc.server.data.Track;
import cool.disc.server.model.Song;
import cool.disc.server.store.song.SongStore;
import javafx.util.Pair;
import okio.ByteString;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public class APIHandlers {
    private static final Logger LOG = LoggerFactory.getLogger(APIHandlers.class);
    private final ObjectMapper objectMapper;
    private SongStore songStore;
    List<Song> songs = null;

    public APIHandlers(final ObjectMapper objectMapper, SongStore songStore) {
        this.objectMapper = objectMapper;
        this.songStore = songStore;
    }

    public Stream<Route<AsyncHandler<Response<ByteString>>>> routes() {
        return java.util.stream.Stream.of(
            // type: tracks, artists
            // title: querying string
                Route.sync("GET", "/song/<title>", this::getSongUrl).withMiddleware(jsonMiddleware()),
                Route.sync("GET", "/song/recommend/<genre>", this::getRecommendations).withMiddleware(jsonMiddleware())
        );
    }

    public JSONObject getRecommendations(final RequestContext requestContext) {
        JSONObject result = null;
        String genre = requestContext.pathArgs().get("genre");
        try {
            Track track = new Track(objectMapper, "", genre);
            Pair<String, String> response = track.searchRecommendations(genre);
            String[] resp = response.toString().split("=");
            result= new JSONObject().put(resp[0],resp[1]);
            return result;
        } catch (IOException | UnirestException e) {
            LOG.error(e.getMessage());
            return null;
        }
    }

    // getSongUrl: retrieves the first song(from album) url in the list
    public List<String> getSongUrl(final RequestContext requestContext) {
        List<String> result = null;
//        String type = requestContext.pathArgs().get("type");
        String type = "track";
        String title = requestContext.pathArgs().get("title");
        LOG.info("type: " + type + "/ title: " + title);
        try {
            Track track = new Track(objectMapper, title, type);
            songs = track.searchSongs();
            for(Song song : songs) {
                // add to database searched songs
                Response<Object> response = songStore.addSong(song);
                LOG.info("response for addSong: {}", response.status().code());
                if(response.status().code() == 200) {                 // success
                    String url = songs.iterator().next().songUrl();
                    result.add(url);
                    LOG.info("urls: {}", result);
                    return result;
                } else if (response.status().code() == 302) {        // found
                    result.add("Track already in DB. Updated score.");
                    return result;
                }
            }
        } catch (IOException | UnirestException e) {
            e.printStackTrace();
        }
        result.add("Track Not Found.");
        return result;
    }

    //     Asynchronous Middleware Handling for payloads
    private <T> Middleware<AsyncHandler<T>, AsyncHandler<Response<ByteString>>> jsonMiddleware() {
        return JsonSerializerMiddlewares.<T>jsonSerialize(objectMapper.writer())
                .and(Middlewares::httpPayloadSemantics)
                .and(
                        responseAsyncHandler ->
                                requestContext ->
                                        responseAsyncHandler
                                                .invoke(requestContext)
                                                .thenApply(
                                                        response -> response.withHeader("Access-Control-Allow-Origin", "*")));
    }
}
