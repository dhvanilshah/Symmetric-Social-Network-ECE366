package cool.disc.server.handler.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify.apollo.RequestContext;
import com.spotify.apollo.Response;
import com.spotify.apollo.route.*;
import cool.disc.server.model.Post;
import cool.disc.server.store.post.PostStore;
import okio.ByteString;

import java.util.List;
import java.util.stream.Stream;

public class PostHandlers {

    private final ObjectMapper objectMapper;
    private PostStore postStore;

    public PostHandlers(final ObjectMapper objectMapper, final PostStore postStore) {
        this.objectMapper = objectMapper;
        this.postStore = postStore;
    }

    public Stream<Route<AsyncHandler<Response<ByteString>>>> routes() {
        return Stream.of(
                Route.sync("GET", "/getPosts?<userId>", this::getPosts)
                        .withMiddleware(jsonMiddleware()),
                Route.sync("GET", "/getFeed", this::getFeed)
                        .withMiddleware(jsonMiddleware()),
                // writerId, receiverId are just for time being (until we can retrieve them from the token)
                Route.sync("POST", "/addPost?<writerId>&<receiverId>&<message>", this::addPost)
                        .withMiddleware(jsonMiddleware()),
                // for testing
                Route.sync("GET", "/getAllPosts", this::geAllPosts).withMiddleware(jsonMiddleware()));
    }

    List<Post> geAllPosts(final RequestContext requestContext) {
        return postStore.getAllPosts();
    }

    // perhaps, adding posts(and users as well) can return http response (200 / 503)
    // this doesn't need to return any data types however
    Post addPost(final RequestContext requestContext) {
        String message = requestContext.pathArgs().get("message");
        String writerId = requestContext.pathArgs().get("writerID");
        String receiverId = requestContext.pathArgs().get("receiverId");
        postStore.addPost(writerId, receiverId, message);
        return null;
    }

    List<Post> getPosts(final RequestContext requestContext) {
        String userId = requestContext.pathArgs().get("userId");
        List<Post> posts = postStore.getPosts(userId);
        return posts;
    }

    List<Post> getFeed(final RequestContext requestContext) {
        List<Post> posts = postStore.getFeed();
        return posts;
    }

    // Asynchronous Middleware Handling for payloads
    private <T> Middleware<AsyncHandler<T>, AsyncHandler<Response<ByteString>>> jsonMiddleware() {
        return JsonSerializerMiddlewares.<T>jsonSerialize(objectMapper.writer())
                .and(Middlewares::httpPayloadSemantics)
                .and(
                        responseAsyncHandler ->
                                requestContext ->
                                        responseAsyncHandler
                                                .invoke(requestContext)
                                                .thenApply(
                                                        response -> response.withHeader("Access-Control-Allow-Origin", "*")));
    }

}