package cool.disc.server.handler.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import cool.disc.server.model.User;
import cool.disc.server.store.user.UserStore;
import com.spotify.apollo.RequestContext;
import com.spotify.apollo.Response;
import com.spotify.apollo.route.AsyncHandler;
import com.spotify.apollo.route.JsonSerializerMiddlewares;
import com.spotify.apollo.route.Middleware;
import com.spotify.apollo.route.Middlewares;
import com.spotify.apollo.route.Route;
import okio.ByteString;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class UserHandlers {

    private final ObjectMapper objectMapper;
    private final UserStore userStore;

    public UserHandlers(final ObjectMapper objectMapper, final UserStore userStore){
        this.objectMapper = objectMapper;
        this.userStore = userStore;
    }

    public Stream<Route<AsyncHandler<Response<ByteString>>>> routes() {
        return Stream.of(
                Route.sync("POST", "/addUser", this::addUser).withMiddleware(jsonMiddleware()),
                Route.sync("GET", "/getUser/<name>", this::getUser).withMiddleware(jsonMiddleware())
        );
    }

    User addUser(final RequestContext requestContext) {
        Optional<String> username = requestContext.request().parameter("username");
        Optional<String> name = requestContext.request().parameter("name");
        Optional<String> password = requestContext.request().parameter("password");
        Optional<String> service = requestContext.request().parameter("service");
        Optional<String> photo = requestContext.request().parameter("photo");
        return userStore.addUser(username.get(), name.get(), password.get(), service.get(), photo.get());
    }

    List<User> getUser(final RequestContext requestContext){
        String name = requestContext.pathArgs().get("name");
        return userStore.getUser(name);
    }

    private <T> Middleware<AsyncHandler<T>, AsyncHandler<Response<ByteString>>> jsonMiddleware() {
        return JsonSerializerMiddlewares.<T>jsonSerialize(objectMapper.writer())
                .and(Middlewares::httpPayloadSemantics)
                .and(responseAsyncHandler -> requestContext ->
                        responseAsyncHandler.invoke(requestContext)
                                .thenApply(response -> response.withHeader("Access-Control-Allow-Origin", "*")));
    }
}
