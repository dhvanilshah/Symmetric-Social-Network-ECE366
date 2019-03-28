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
                Route.sync("GET", "/getPostFrom/<first>/<last>", this::getPostFrom)
                        .withMiddleware(jsonMiddleware()),
                Route.sync("GET", "/getPostTo/<first>/<last>", this::getPostTo)
                        .withMiddleware(jsonMiddleware()),
                Route.sync("GET", "/getAllPosts", this::geAllPosts).withMiddleware(jsonMiddleware()));
    }

    List<Post> geAllPosts(final RequestContext requestContext) {
        return postStore.getAllPosts();
    }

    List<Post> getPostFrom(final RequestContext requestContext) {
        String firstname = requestContext.pathArgs().get("first");
        String lastname = requestContext.pathArgs().get("last");
        List<Post> posts = postStore.getPostFrom(firstname, lastname);
        return posts;
    }

    List<Post> getPostTo(final RequestContext requestContext) {
        String firstname = requestContext.pathArgs().get("first");
        String lastname = requestContext.pathArgs().get("last");
        List<Post> posts = postStore.getPostFrom(firstname, lastname);
        return posts;
    }

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