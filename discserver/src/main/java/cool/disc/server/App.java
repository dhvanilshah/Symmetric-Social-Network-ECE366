package cool.disc.server;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify.apollo.Environment;
import com.spotify.apollo.httpservice.HttpService;
import com.spotify.apollo.httpservice.LoadingException;
import com.spotify.apollo.route.Route;
import cool.disc.server.handler.post.PostHandlers;
import cool.disc.server.handler.user.UserHandlers;
import cool.disc.server.model.User;
import cool.disc.server.store.post.PostStore;
import cool.disc.server.store.post.PostStoreController;
import cool.disc.server.store.user.UserStore;
import cool.disc.server.store.user.UserStoreController;
import io.norberg.automatter.jackson.AutoMatterModule;

import java.util.List;

public final class App {

    public static void main(String[] args) throws LoadingException {
        HttpService.boot(App::init, "disc", args);
    }

    static void init(Environment environment) {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new AutoMatterModule()).setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);;
        UserStore userStore = new UserStoreController(environment.config());
        UserHandlers userHandlers = new UserHandlers(objectMapper, userStore);
        userHandlers.routes();

        PostStore postStore = new PostStoreController(environment.config());
        PostHandlers postHandlers = new PostHandlers(objectMapper, postStore, userStore);
        postHandlers.routes();

        environment.routingEngine()
                .registerAutoRoute(Route.sync("GET", "/", rc -> "hello world"))
                .registerRoutes(userHandlers.routes())
                .registerRoutes(postHandlers.routes());
    }
}