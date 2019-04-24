package cool.disc.server.handler.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClientException;
import com.spotify.apollo.RequestContext;
import com.spotify.apollo.Response;
import com.spotify.apollo.route.*;
import cool.disc.server.model.Post;
import cool.disc.server.store.post.PostStore;
import cool.disc.server.store.user.UserStore;
import okio.ByteString;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class PostHandlers {

    private final ObjectMapper objectMapper;
    private PostStore postStore;
    private UserStore userStore;

    public PostHandlers(final ObjectMapper objectMapper, final PostStore postStore, final UserStore userStore) {
        this.objectMapper = objectMapper;
        this.postStore = postStore;
        this.userStore = userStore;
    }

    public Stream<Route<AsyncHandler<Response<ByteString>>>> routes() {
        return Stream.of(
                Route.sync("GET", "/getFeed", this::getFeed).withMiddleware(jsonMiddleware()),
                Route.sync("POST", "/addPost", this::addPost).withMiddleware(jsonMiddleware()),
                Route.sync("GET", "/getAllPosts", this::getAllPosts).withMiddleware(jsonMiddleware())
        );
    }

    // retrieves all posts in the database
    List<Post> getAllPosts(final RequestContext requestContext) {
        return postStore.getAllPosts();
    }

    // adding a post from parameters received through http request
    // parameters: writerName (not id), receiverName (not id), message
    Integer addPost(final RequestContext requestContext) {
        Post post;
        if (requestContext.request().payload().isPresent()) {
            try {
                post = objectMapper.readValue(requestContext.request().payload().get().toByteArray(), Post.class);
                Response<Object> response = postStore.addPost(post);
                return response.status().code();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("invalid payload");
            }
        } else {
            throw new RuntimeException("no payload");
        }
    }

    // retrieves all posts written by the specified user and the user's friends.
    // needs to pass in the name of the user as parameter through http request
    List<Post> getFeed(final RequestContext requestContext) {
        String name = requestContext.request().parameters().get("name").iterator().next();
        List<Post> postList = new ArrayList<>();
        try {
            // check if the name - userId exists
            ObjectId postId = userStore.getUserId(name);
            if (postId != null) { // if in the database
                postList = postStore.getFeed(name);
                if (postList.size() != 0) {
                    System.out.println("postList found entries!");
                    return postList;
                }
            }
        } catch (MongoClientException e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        return postList;
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