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
        this.authUtils = authUtils;     /* Added this because Ethan suggested we do it this way */
    }

    public Stream<Route<AsyncHandler<Response<ByteString>>>> routes() {
        return Stream.of(
                Route.sync("POST", "/addUser", this::addUser).withMiddleware(jsonMiddleware()),
                Route.sync("GET", "/getUser/<name>", this::getUser).withMiddleware(jsonMiddleware()),
                Route.sync("OPTIONS", "/getUser/<any>", rc -> "ok").withMiddleware(jsonMiddleware()),
                Route.sync("GET", "/login", this::login).withMiddleware(jsonMiddleware()),
                Route.sync("OPTIONS", "/login", rc -> "ok").withMiddleware(jsonMiddleware()),
                Route.sync("GET", "/addFriend/<id>", this::addFriend).withMiddleware(jsonMiddleware()),
                Route.sync("OPTIONS", "/addFriend/<id>", rc -> "ok").withMiddleware(jsonMiddleware()),
                Route.sync("GET", "/handleRequest/<id>/<action>", this::acceptRequest).withMiddleware(jsonMiddleware()),
                Route.sync("OPTIONS", "/handleRequest/<id>/<action>", rc -> "ok").withMiddleware(jsonMiddleware()),
                Route.sync("GET", "/getRequests", this::getRequests).withMiddleware(jsonMiddleware()),
                Route.sync("OPTIONS", "/getRequests", rc -> "ok").withMiddleware(jsonMiddleware()),
                Route.sync("GET", "/getFriends", this::getFriends).withMiddleware(jsonMiddleware()),
                Route.sync("OPTIONS", "/getFriends", rc -> "ok").withMiddleware(jsonMiddleware()),
                Route.sync("GET", "/getBio/<username>", this::getBio).withMiddleware(jsonMiddleware()),
                Route.sync("OPTIONS", "/getBio/<username>", rc -> "ok").withMiddleware(jsonMiddleware()),
                Route.sync("POST", "/updateBio", this::updateBio).withMiddleware(jsonMiddleware()),
                Route.sync("OPTIONS", "/updateBio", rc -> "ok").withMiddleware(jsonMiddleware())
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

    public Response<List<User>> getUser(final RequestContext requestContext){ Optional<String> token = requestContext.request().header("session-token");
        String name = requestContext.pathArgs().get("name");
        List<User> data = userStore.getUser(name);
        return Response.ok().withPayload(data);
    }


    public Response<String> login(final RequestContext requestContext){

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

    @SuppressWarnings("Duplicates")
    public Response<String> addFriend(final RequestContext requestContext){
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

    @SuppressWarnings("Duplicates")
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

    @SuppressWarnings("Duplicates")
    public Response<List<User>> getRequests(final RequestContext requestContext){
        Optional<String> token = requestContext.request().header("session-token");
        if (!token.isPresent()) {
            return Response.forStatus(Status.BAD_REQUEST);
        }
        String user_id = authUtils.verifyToken(token.get());

        if(user_id == null){
            return Response.forStatus(Status.UNAUTHORIZED);
        }

        List<User>  requestSenders = userStore.getRequests(user_id);

        return Response.ok().withPayload(requestSenders);
    }

    @SuppressWarnings("Duplicates")
    public Response<List<User>> getFriends(final RequestContext requestContext){
        Optional<String> token = requestContext.request().header("session-token");
        if (!token.isPresent()) {
            return Response.forStatus(Status.BAD_REQUEST);
        }
        String user_id = authUtils.verifyToken(token.get());

        if(user_id == null){
            return Response.forStatus(Status.UNAUTHORIZED);
        }

        List<User>  requestSenders = userStore.getFriends(user_id);

        return Response.ok().withPayload(requestSenders);
    }

    @SuppressWarnings("Duplicates")
    public Response<User> getBio(final RequestContext requestContext){
        Optional<String> token = requestContext.request().header("session-token");
        String username = requestContext.pathArgs().get("username");
        if (!token.isPresent()) {
            return Response.forStatus(Status.BAD_REQUEST);
        }
        String user_id = authUtils.verifyToken(token.get());

        if(user_id == null){
            return Response.forStatus(Status.UNAUTHORIZED);
        }

        User user = userStore.getBio(user_id, username);

        return Response.ok().withPayload(user);
    }

    @SuppressWarnings("Duplicates")
    public Response<User> updateBio(final RequestContext requestContext){
        Optional<String> token = requestContext.request().header("session-token");
        if (!token.isPresent()) {
            return Response.forStatus(Status.BAD_REQUEST);
        }
        String user_id = authUtils.verifyToken(token.get());

        if(user_id == null){
            return Response.forStatus(Status.UNAUTHORIZED);
        }

        User user;
        JsonNode test;

        if (requestContext.request().payload().isPresent()) {
            try {
                test = objectMapper.readTree(requestContext.request().payload().get().utf8());
                user = objectMapper.readValue(test.toString(), User.class);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("invalid payload");
            }
        } else {
            throw new RuntimeException("no payload");
        }

        Integer resp = userStore.updateBio(user_id, user);

        return Response.ok();
    }

    @SuppressWarnings("Duplicates")
    private <T> Middleware<AsyncHandler<T>, AsyncHandler<Response<ByteString>>> jsonMiddleware() {

        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        headers.put("Access-Control-Allow-Headers", "origin, content-type, accept, x-requested-with, session-token");
        headers.put("Access-Control-Max-Age", "3600");

        return JsonSerializerMiddlewares.<T>jsonSerialize(objectMapper.writer())
                .and(Middlewares::httpPayloadSemantics)
                .and(responseAsyncHandler -> requestContext ->
                        responseAsyncHandler.invoke(requestContext)
                                .thenApply(response -> response.withHeaders(headers)));
    }
}