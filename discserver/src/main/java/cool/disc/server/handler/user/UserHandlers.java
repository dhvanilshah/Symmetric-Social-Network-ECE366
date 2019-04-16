package cool.disc.server.handler.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify.apollo.Status;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
                Route.sync("GET", "/addUser", this::addUser).withMiddleware(jsonMiddleware()),
                Route.sync("GET", "/getUser/<name>", this::getUser).withMiddleware(jsonMiddleware()),
                Route.sync("GET", "/login", this::login).withMiddleware(jsonMiddleware())
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

    Response<String> login(final RequestContext requestContext){

        Optional<String> username = requestContext.request().parameter("username");
        Optional<String> password = requestContext.request().parameter("password");

        String response = userStore.login(username.get(), password.get());

        if(response.equals("invalid")){
            return Response.of(Status.UNAUTHORIZED, "Invalid username or password");
        }
        else{
            return Response.ok().withPayload(response);
        }
    }



    private <T> Middleware<AsyncHandler<T>, AsyncHandler<Response<ByteString>>> jsonMiddleware() {

        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Methods", "GET, POST");

        return JsonSerializerMiddlewares.<T>jsonSerialize(objectMapper.writer())
                .and(Middlewares::httpPayloadSemantics)
                .and(responseAsyncHandler -> requestContext ->
                        responseAsyncHandler.invoke(requestContext)
                                .thenApply(response -> response.withHeaders(headers)));
    }
}
