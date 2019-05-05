package cool.disc.server.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify.apollo.Environment;
import com.spotify.apollo.RequestContext;
import com.spotify.apollo.Response;
import com.spotify.apollo.httpservice.HttpService;
import com.spotify.apollo.httpservice.LoadingException;
import com.spotify.apollo.route.Route;
import cool.disc.server.test.handler.auth.APIHandlers;
import cool.disc.server.test.handler.post.PostHandlers;
import cool.disc.server.test.handler.user.UserHandlers;
import cool.disc.server.test.store.post.PostStore;
import cool.disc.server.test.store.post.PostStoreController;
import cool.disc.server.test.store.song.SongStore;
import cool.disc.server.test.store.song.SongStoreController;
import cool.disc.server.test.store.user.UserStore;
import cool.disc.server.test.store.user.UserStoreController;
import cool.disc.server.test.utils.AuthUtils;
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
//        ObjectMapper objectMapper = new ObjectMapper().registerModule(new AutoMatterModule());

//        AlbumResource albumResource = new AlbumResource(objectMapper);
//        ArtistResource artistResource = new ArtistResource(objectMapper);


        /* Added this because Ethan said it should not be constructed within the userHandlers */
        AuthUtils authUtils = new AuthUtils();

        UserStore userStore = new UserStoreController();
        UserHandlers userHandlers = new UserHandlers(objectMapper, userStore, authUtils);

        PostStore postStore = new PostStoreController();
        PostHandlers postHandlers = new PostHandlers(objectMapper, postStore, userStore, authUtils);

        SongStore songStore = new SongStoreController();
        APIHandlers apiHandlers = new APIHandlers(objectMapper, songStore);

        // "/ping" for the purpose of checking if routing works only
        environment.routingEngine()
                .registerAutoRoute(Route.sync("GET", "/", rc -> "Welcome to Backend!"))
                .registerAutoRoute(Route.sync("OPTIONS", "*", rc -> "OK"))
                .registerRoutes(userHandlers.routes())
                .registerRoutes(postHandlers.routes())
                .registerRoutes(apiHandlers.routes())
                .registerRoute(Route.sync("GET", "/ping", App::ping).withDocString(
                        "Responds with a 'pong!' if the service is up.",
                        "Useful endpoint for doing health checks."));
    }

//     for testing health checks
    public static Response<ByteString> ping(RequestContext requestContext) {
        return Response.ok().withPayload(ByteString.encodeUtf8("pong!"));
    }
}