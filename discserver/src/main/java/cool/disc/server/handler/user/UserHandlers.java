package cool.disc.server.handler.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify.apollo.RequestContext;
import com.spotify.apollo.Response;
import com.spotify.apollo.Status;
import com.spotify.apollo.route.*;
import cool.disc.server.model.User;
import cool.disc.server.store.user.UserStore;
import cool.disc.server.utils.AuthUtils;
import okio.ByteString;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class UserHandlers {

    private final ObjectMapper objectMapper;
    private final UserStore userStore;
    private final AuthUtils authUtils;

    public UserHandlers(final ObjectMapper objectMapper, final UserStore userStore, final AuthUtils authUtils){
        this.objectMapper = objectMapper;
        this.userStore = userStore;
        this.authUtils = authUtils;
    }

    public Stream<Route<AsyncHandler<Response<ByteString>>>> routes() {
        return Stream.of(
                Route.sync("OPTIONS", "/addUser", rc -> "hello").withMiddleware(jsonMiddleware()),
                Route.sync("POST", "/addUser", this::addUser).withMiddleware(jsonMiddleware()),
                Route.sync("GET", "/getUser/<name>", this::getUser).withMiddleware(jsonMiddleware()),
                Route.sync("GET", "/login", this::login).withMiddleware(jsonMiddleware()),
                Route.sync("GET", "/addFriend/<id>", this::addFriend).withMiddleware(jsonMiddleware()),
                Route.sync("GET", "/handleRequest/<id>/<action>", this::acceptRequest).withMiddleware(jsonMiddleware())
        );
    }

    public Response addUser(final RequestContext requestContext) {
        User user;
        Integer response;
        JsonNode test;

        if (requestContext.request().payload().isPresent()) {
            try {
                test = objectMapper.readTree(requestContext.request().payload().get().utf8());
                user = objectMapper.readValue(test.toString(), User.class);
                response = userStore.addUser(user);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("invalid payload");
            }
        } else {
            throw new RuntimeException("no payload");
        }

        if(response == 1){
            return Response.ok();
        }
        else {
            return Response.of(Status.UNAUTHORIZED, ByteString.encodeUtf8("Could not create account"));
        }
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

    Response<String> addFriend(final RequestContext requestContext){
        String friend_id = requestContext.pathArgs().get("id");
        Optional<String> token = requestContext.request().header("session-token");
        if (friend_id.isEmpty() || !token.isPresent()) {
            return Response.of(Status.BAD_REQUEST, "Invalid request");
        }
        String user_id = authUtils.verifyToken(token.get());

        if(user_id == null){
            return Response.of(Status.UNAUTHORIZED, "Could not verify user.");
        }
        String response = userStore.addFriend(friend_id, user_id);

        return Response.ok().withPayload(response);
    }

    Response<String> acceptRequest(final RequestContext requestContext){
        String friend_id = requestContext.pathArgs().get("id");
        String action = requestContext.pathArgs().get("action");
        Optional<String> token = requestContext.request().header("session-token");
        if (friend_id.isEmpty() || !token.isPresent()) {
            return Response.of(Status.BAD_REQUEST, "Invalid request");
        }
        String user_id = authUtils.verifyToken(token.get());

        if(user_id == null){
            return Response.of(Status.UNAUTHORIZED, "Could not verify user.");
        }
        String response = userStore.handleRequest(friend_id, user_id, action);

        return Response.ok().withPayload(response);
    }


    private <T> Middleware<AsyncHandler<T>, AsyncHandler<Response<ByteString>>> jsonMiddleware() {

        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Methods", "GET, POST, OPTIONS");

        return JsonSerializerMiddlewares.<T>jsonSerialize(objectMapper.writer())
                .and(Middlewares::httpPayloadSemantics)
                .and(responseAsyncHandler -> requestContext ->
                        responseAsyncHandler.invoke(requestContext)
                                .thenApply(response -> response.withHeaders(headers)));
    }
}