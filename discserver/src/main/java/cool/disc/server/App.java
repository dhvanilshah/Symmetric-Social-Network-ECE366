package cool.disc.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify.apollo.Environment;
import com.spotify.apollo.httpservice.HttpService;
import com.spotify.apollo.httpservice.LoadingException;
import com.spotify.apollo.route.Route;
import cool.disc.server.handler.user.UserHandlers;
import cool.disc.server.store.user.UserStore;
import cool.disc.server.store.user.UserStoreController;
import io.norberg.automatter.jackson.AutoMatterModule;

public final class App {

    public static void main(String[] args) throws LoadingException {
        HttpService.boot(App::init, "disc", args);
    }

    static void init(Environment environment) {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new AutoMatterModule());
        UserStore userStore = new UserStoreController();
        UserHandlers userHandlers = new UserHandlers(objectMapper, userStore);
        userHandlers.routes();

        environment.routingEngine()
                .registerAutoRoute(Route.sync("GET", "/hello", rc -> "hello world"))
                .registerRoutes(userHandlers.routes());
    }
}