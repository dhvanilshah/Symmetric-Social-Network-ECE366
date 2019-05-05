package cool.disc.server.handler.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.spotify.apollo.RequestContext;
import com.spotify.apollo.Response;
import com.spotify.apollo.Status;
import com.spotify.apollo.route.*;
import cool.disc.server.data.Track;
import cool.disc.server.model.Song;
import cool.disc.server.store.song.SongStore;
import okio.ByteString;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class APIHandlers {
    private static final Logger LOG = LoggerFactory.getLogger(APIHandlers.class);
    private final ObjectMapper objectMapper;
    private SongStore songStore;
    List<Song> songs;
    List<JSONObject> result;

    public APIHandlers(final ObjectMapper objectMapper, SongStore songStore) {
        this.objectMapper = objectMapper;
        this.songStore = songStore;
    }

    public Stream<Route<AsyncHandler<Response<ByteString>>>> routes() {
        return java.util.stream.Stream.of(
            // type: tracks, artists
            // title: querying string
                Route.sync("GET", "/song/<title>", this::getSongInfo).withMiddleware(jsonMiddleware()),                     // input: pathArg
                Route.sync("OPTIONS", "/song/<title>", rc -> "ok").withMiddleware(jsonMiddleware()),
                Route.sync("GET", "/song/recommend/<title>", this::getRecommendations).withMiddleware(jsonMiddleware()),  // input: pathArg
                Route.sync("OPTIONS", "/song/recommend/<title>", rc -> "ok").withMiddleware(jsonMiddleware()),
                Route.sync("POST", "/song/add", this::addSong).withMiddleware(jsonMiddleware()),                              // input: payload
                Route.sync("OPTIONS", "/song/add", rc -> "ok").withMiddleware(jsonMiddleware())
        );
    }

    public List<JSONObject> getRecommendations(final RequestContext requestContext) {
        List<JSONObject> result = new ArrayList<>();
        String title = requestContext.pathArgs().get("title");
        String genre = "world-music";
        try {
            Track track = new Track(objectMapper, title, genre);
            // todo: return list of songs
            songs = track.searchRecommendations(title, genre);
            result = makeJSONList(songs);
        } catch (IOException | UnirestException e) {
            LOG.error("Error: {}", e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    // getSongInfo: returns a list of searched songs
    public List<JSONObject> getSongInfo(final RequestContext requestContext) {
        List<JSONObject> result = new ArrayList<>();
        String type = "track";
        String title = requestContext.pathArgs().get("title");
        try {
            Track track = new Track(objectMapper, title, type);
            songs = track.searchSongs();
            result = makeJSONList(songs);
        } catch (IOException | UnirestException e) {
            LOG.error("Error: {}", e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    // addSong: adds a song to the database collection
    public Response<Object> addSong(final RequestContext requestContext) {
        Song song;
        JsonNode songVal;
        if (requestContext.request().payload().isPresent()) {
            try {
                // JSON Document
                songVal = objectMapper.readTree(requestContext.request().payload().get().utf8());
                // Values from JSON --> Song class
                song = objectMapper.readValue(songVal.toString(), Song.class);
                Response<Object> response = songStore.addSong(song);
                if(response.status().code() == 200) {
                    LOG.info("addSong -- {}", response);
                    return response;
                } else if (response.status().code() == 302) {
                    LOG.info("addSong -- Track already in DB. Updated score.");
                    return Response.forStatus(Status.FOUND);
                } else if (response.status().code() == 404){
                    LOG.info("addSong -- Track Not Found");
                    return Response.forStatus(Status.NOT_FOUND);
                } else {
                    LOG.info("addSong -- Misc. Error");
                    return Response.forStatus(Status.CONFLICT);
                }
            }
            catch(Exception e) {
                LOG.error("addSong Error: {}", e.getMessage());
            }
        }
        return null;
    }

    // utility function to generate JSON List to return from obtained Songs
    private List<JSONObject> makeJSONList(List<Song> songs) {
        List<JSONObject> result = new ArrayList<>();
        for(Song song : songs) {
            String name = song.title();
            String artist = song.artist();
            String album = song.albumImageUrl();
            String url = song.songUrl();
            JSONObject songInfo = new JSONObject();
            songInfo.put("title", name).put("artist",artist).put("album", album).put("url", url);
            result.add(songInfo);
        }
        return result;
    }

    //     Asynchronous Middleware Handling for payloads
    @SuppressWarnings("Duplicates")
    private <T> Middleware<AsyncHandler<T>, AsyncHandler<Response<ByteString>>> jsonMiddleware() {

        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        headers.put("Access-Control-Allow-Headers", "origin, content-type, accept, x-requested-with, session-token");
        headers.put("Access-Control-Max-Age", "3600");

        return JsonSerializerMiddlewares.<T>jsonSerialize(objectMapper.writer())
                .and(Middlewares::httpPayloadSemantics)
                .and(responseAsyncHandler -> requestContext ->
                        responseAsyncHandler.invoke(requestContext)
                                .thenApply(response -> response.withHeaders(headers)));
    }
}
