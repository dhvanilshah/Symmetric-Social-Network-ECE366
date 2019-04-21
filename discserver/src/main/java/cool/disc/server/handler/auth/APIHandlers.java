package cool.disc.server.handler.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.spotify.apollo.RequestContext;
import com.spotify.apollo.Response;
import com.spotify.apollo.route.*;
import cool.disc.server.data.Track;
import cool.disc.server.model.Song;
import cool.disc.server.store.song.SongStore;
import okio.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public class APIHandlers {
    private static final Logger LOG = LoggerFactory.getLogger(APIHandlers.class);
    private final ObjectMapper objectMapper;
    private SongStore songStore;

    public APIHandlers(final ObjectMapper objectMapper, SongStore songStore) {
        this.objectMapper = objectMapper;
        this.songStore = songStore;
    }

    public Stream<Route<AsyncHandler<Response<ByteString>>>> routes() {
        return java.util.stream.Stream.of(
                Route.sync("GET", "/song/<type>/<title>", this::getSongUrl).withMiddleware(jsonMiddleware())
        );
    }

    public String getSongUrl(final RequestContext requestContext) {
        String type = requestContext.pathArgs().get("type");
        String title = requestContext.pathArgs().get("title");
        LOG.info("type: " + type + "title: " + title);
        try {
            Track track = new Track(objectMapper, title, type);
            List<Song> songs = track.searchSongs();
            for(Song song : songs) {
                Response<Object> response = songStore.addSong(song);
                LOG.info("response for addSong: {}", response.status().code());
                if(response.status().code() == 200) {                 // success
                    String url = songs.iterator().next().songUrl();
                    LOG.info("url : {}", url);
                    return url;
                }
            }
        } catch (IOException | UnirestException e) {
            e.printStackTrace();
        }
        return null;
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
