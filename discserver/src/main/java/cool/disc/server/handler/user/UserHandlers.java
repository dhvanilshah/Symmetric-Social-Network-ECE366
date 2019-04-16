package cool.disc.server.handler.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify.apollo.Status;
import cool.disc.server.model.User;
import cool.disc.server.store.user.UserStore;
import com.spotify.apollo.RequestContext;
import com.spotify.apollo.Response;
import com.spotify.apollo.route.*;
import cool.disc.server.model.User;
import cool.disc.server.store.user.UserStore;
import okio.ByteString;

import java.util.HashMap;
import java.io.IOException;
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

    Integer addUser(final RequestContext requestContext) {
        User user;
        if (requestContext.request().payload().isPresent()) {
            try {
                user = objectMapper.readValue(requestContext.request().payload().get().toByteArray(), User.class);
                Response<Object> response = userStore.addUser(user);
                return response.status().code();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("invalid payload");
            }
        } else {
            throw new RuntimeException("no payload");
        }
    }

    List<User> getUser(final RequestContext requestContext){
        String name = requestContext.pathArgs().get("name");
        return userStore.getUser(name);
    }


    Response<ByteString> login(final RequestContext requestContext){

        Optional<String> username = requestContext.request().parameter("username");
        Optional<String> password = requestContext.request().parameter("password");

        String response = userStore.login(username.get(), password.get());

        if(response.equals("invalid")){
            return Response.of(Status.UNAUTHORIZED, ByteString.encodeUtf8("Invalid username or password"));
        }
        else{
            return Response.ok().withPayload(ByteString.encodeUtf8(response));
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
