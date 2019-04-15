package cool.disc.server.handler.album;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.spotify.apollo.Client;
import com.spotify.apollo.Request;
import com.spotify.apollo.RequestContext;
import com.spotify.apollo.Response;
import com.spotify.apollo.route.AsyncHandler;
import com.spotify.apollo.route.JsonSerializerMiddlewares;
import com.spotify.apollo.route.Route;
import cool.disc.server.data.Album;
import cool.disc.server.data.Artist;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import okio.ByteString;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.PublicKey;
import java.security.interfaces.RSAKey;
import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

/**
 * The album resource demonstrates how to use asynchronous routes in Apollo.
 */
public class AlbumResource {

    private static final String SPOTIFY_API = "https://api.spotify.com";
    private static final String SEARCH_API = SPOTIFY_API + "/v1/search";
    private static final String ALBUM_API = SPOTIFY_API + "/v1/albums";

    private final ObjectMapper objectMapper;
    private final ObjectWriter objectWriter;

    public AlbumResource(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.objectWriter = objectMapper.writer();

        try {
            HttpResponse<String> response = Unirest.post("https://dev-3c319w1a.auth0.com/oauth/token")
                    .header("content-type", "application/json")
                    .body("{\"grant_type\":\"client_credentials\",\"client_id\": \"Iwayq1OdMkvEnRtKQDc2tCqLP7BbnDPs\",\"client_secret\": \"HkV24nAklI0LZqeki-V1Kal5U4tPPueNyi1EZgH8h0_AE6zP1Pt6vYQzsTyer1Va\",\"audience\": \"https://api.spotify.com/\"}")
                    .asString();
            String header = response.getBody().substring(16,137);
            String body = response.getBody().substring(138,438);
            String signature = response.getBody().substring(439,781);
            String jwt = header+"."+body+"."+signature;

            HttpResponse<String> certificateResponse = Unirest.get("https://dev-3c319w1a.auth0.com/.well-known/jwks.json")
                    .header("content-type", "application/json")
                    .asString();
            String publicKey = (String) certificateResponse.getBody().subSequence(56, 1583);

        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    public Stream<Route<AsyncHandler<Response<ByteString>>>> routes() {



        // The album resource has two routes. Since both are returning the same type,
        // we can map them to the same middleware
        // Note that this could also have been set up as a single route to "/albums/<tag>".
        return Stream.of(
                Route.async("GET", "/albums/new", context -> getAlbums(context, "new")).withDocString(
                        "Get the latest albums on Spotify.",
                        "Uses the public Spotify API https://api.spotify.com to get 'new' albums."),
                Route.async("GET", "/albums/hipster", context -> getAlbums(context, "hipster")).withDocString(
                        "Get hipster albums on Spotify.",
                        "Uses the public Spotify API https://api.spotify.com to get albums with the keyword 'hipster'.")
        )
                .map(route -> route.withMiddleware(
                        JsonSerializerMiddlewares.jsonSerializeResponse(objectWriter)));
    }

    CompletionStage<Response<ArrayList<Album>>> getAlbums(RequestContext requestContext, String tag) {
        // We need to first query the search API, parse the result, then query the album API.
        Request searchRequest = Request.forUri(SEARCH_API + "?type=album&q=tag%3A" + tag);
        Client client = requestContext.requestScopedClient();
        return client
                .send(searchRequest)
                .thenComposeAsync(response -> {
                    String ids = parseResponseAlbumIds(response.payload().get().utf8());
                    return client.send(Request.forUri(ALBUM_API + "?ids=" + ids));
                })
                .thenApplyAsync(response -> Response.ok()
                        .withPayload(parseAlbumData(response.payload().get().utf8())));
    }

    /**
     * Parses an album response from a
     * <a href="https://developer.spotify.com/web-api/album-endpoints/">Spotify API album query</a>.
     *
     * @param json The json response
     * @return A list of albums with artist information
     */
    private ArrayList<Album> parseAlbumData(String json) {
        ArrayList<Album> albums = new ArrayList<>();
        try {
            JsonNode jsonNode = this.objectMapper.readTree(json);
            for (JsonNode albumNode : jsonNode.get("albums")) {
                JsonNode artistsNode = albumNode.get("artists");
                // Exclude albums with 0 artists
                if (artistsNode.size() >= 1) {
                    // Only keeping the first artist for simplicity
                    Artist artist = new Artist(artistsNode.get(0).get("name").asText());
                    Album album = new Album(albumNode.get("name").asText(), artist);
                    albums.add(album);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse JSON", e);
        }
        return albums;
    }

    /**
     * Parses the album ids from a JSON response from a
     * <a href="https://developer.spotify.com/web-api/search-item/">Spotify API search query</a>.
     *
     * @param json The JSON response
     * @return A comma-separated list of album ids from the response
     */
    private String parseResponseAlbumIds(String json) {
        StringJoiner sj = new StringJoiner(",");
        try {
            JsonNode jsonNode = this.objectMapper.readTree(json);
            for (JsonNode node : jsonNode.get("albums").get("items")) {
                sj.add(node.get("id").asText());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse JSON", e);
        }
        return sj.toString();
    }
}

