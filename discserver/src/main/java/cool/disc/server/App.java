package cool.disc.server;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.spotify.apollo.Environment;
import com.spotify.apollo.RequestContext;
import com.spotify.apollo.Response;
import com.spotify.apollo.httpservice.HttpService;
import com.spotify.apollo.httpservice.LoadingException;
import com.spotify.apollo.route.Route;
import cool.disc.server.data.Album;
import cool.disc.server.handler.album.AlbumResource;
import cool.disc.server.handler.artist.ArtistResource;
import cool.disc.server.handler.post.PostHandlers;
import cool.disc.server.handler.user.UserHandlers;
import cool.disc.server.store.post.PostStore;
import cool.disc.server.store.post.PostStoreController;
import cool.disc.server.store.user.UserStore;
import cool.disc.server.store.user.UserStoreController;
import cool.disc.server.utils.AuthUtils;
import io.norberg.automatter.jackson.AutoMatterModule;
import okio.ByteString;

import java.util.List;

public final class App {
    /**
     * @param args  program arguments passed in from the command line
     * @throws LoadingException if HttpService fails to boot
     */
    public static List<String> urls = null;

    public static void main(String[] args) throws LoadingException {
        HttpService.boot(App::init, "disc", args);
    }

    public static void init(Environment environment) {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new AutoMatterModule()).setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        AlbumResource albumResource = new AlbumResource(objectMapper);
        ArtistResource artistResource = new ArtistResource(objectMapper);

        /* Added this because Ethan said it should not be constructed within the userHandlers */
        AuthUtils authUtils = new AuthUtils();

        UserStore userStore = new UserStoreController();
        UserHandlers userHandlers = new UserHandlers(objectMapper, userStore, authUtils);

        PostStore postStore = new PostStoreController();
        PostHandlers postHandlers = new PostHandlers(objectMapper, postStore, userStore);
        Album album = new Album(objectMapper);
        try {
            List<String> trackUrls = album.getUrls();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        // "/ping" for the purpose of checking if routing works only
        environment.routingEngine()
                .registerAutoRoute(Route.sync("GET", "/", rc -> "Welcome to Backend!"))
                .registerRoutes(userHandlers.routes())
                .registerRoutes(postHandlers.routes())
//                .registerRoutes(albumResource.routes())
//                .registerRoutes(artistResource.routes())
                .registerRoute(Route.sync("GET", "/ping", App::ping).withDocString(
                        "Responds with a 'pong!' if the service is up.",
                        "Useful endpoint for doing health checks."));
    }

//     for testing health checks
    public static Response<ByteString> ping(RequestContext requestContext) {
        return Response.ok().withPayload(ByteString.encodeUtf8("pong!"));
    }
}