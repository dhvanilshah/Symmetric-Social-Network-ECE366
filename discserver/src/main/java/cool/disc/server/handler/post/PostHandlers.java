package cool.disc.server.handler.post;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify.apollo.RequestContext;
import com.spotify.apollo.Response;
import com.spotify.apollo.route.*;
import cool.disc.server.model.Post;
import cool.disc.server.store.post.PostStore;
import cool.disc.server.store.user.UserStore;
import cool.disc.server.utils.AuthUtils;
import okio.ByteString;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

public class PostHandlers {
    private static final Logger LOG = LoggerFactory.getLogger(PostHandlers.class);

    private final ObjectMapper objectMapper;
    private PostStore postStore;
    private UserStore userStore;
    private final AuthUtils authUtils;

    public PostHandlers(final ObjectMapper objectMapper, final PostStore postStore, final UserStore userStore, final AuthUtils authUtils) {
        this.objectMapper = objectMapper;
        this.postStore = postStore;
        this.userStore = userStore;
        this.authUtils = authUtils;
    }

    public Stream<Route<AsyncHandler<Response<ByteString>>>> routes() {
        return Stream.of(
                Route.sync("GET", "/getMyFeed", this::getMyFeed).withMiddleware(jsonMiddleware()),
                Route.sync("OPTIONS", "/getMyFeed", rc -> "ok").withMiddleware(jsonMiddleware()),
                Route.sync("GET", "/getPublicFeed/<username>", this::getPublicFeed).withMiddleware(jsonMiddleware()),
                Route.sync("OPTIONS", "/getPublicFeed/<username>", rc -> "ok").withMiddleware(jsonMiddleware()),
                Route.sync("POST", "/addPost", this::addPost).withMiddleware(jsonMiddleware()),
                Route.sync("OPTIONS", "/addPost", rc -> "ok").withMiddleware(jsonMiddleware())
        );
    }

    @SuppressWarnings("Duplicates")
    // adding a post from parameters received through http request
    // parameters: writerName (not id), receiverName (not id), message
    public Integer addPost(final RequestContext requestContext) {
        Post post;
        JsonNode postVal;

        Optional<String> token = requestContext.request().header("session-token");
        if (!token.isPresent()) {
            return 404;
        }
        String user_id = authUtils.verifyToken(token.get());

        if(user_id == null){
            return 402; //unauthorized
        }

        if (requestContext.request().payload().isPresent()) {
            try {
                postVal = objectMapper.readTree(requestContext.request().payload().get().utf8());
                post = objectMapper.readValue(postVal.toString(), Post.class);
                Response<Object> response = postStore.addPost(post, user_id);
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
  public List<JSONObject> getMyFeed(final RequestContext requestContext) {

      Optional<String> token = requestContext.request().header("session-token");
      String userId = authUtils.verifyToken(token.get());
      List<Post> posts = postStore.getMyFeed(userId);
      List<JSONObject> result = new ArrayList<>();
      if (!posts.isEmpty()) {
          try {
            result = JSONListfromPosts(result, posts);
        } catch (NullPointerException e) {
            LOG.error("null pointer exception: {}", e.getMessage());
            LOG.error("result: {}", result);
            throw new NullPointerException();
          }
      }
        return result;
    }

    private List<JSONObject> JSONListfromPosts(List<JSONObject> result, List<Post> posts) {
        for (Post post : posts) {
          String writerId = post.writerId().toString();
          String receiverId = post.receiverId().toString();
          String album = post.message();
          Integer privacy = post.privacy();
          String message = post.message();
          Integer likes = post.likes();
//          ObjectId songId = post.songId();
          String title = post.title();
          String songUrl = post.songUrl();
          String albumImageUrl = post.albumImageUrl();
          String artist = post.artist();
          Date dateCreated = post.dateCreated();
          String writerName  = post.writerName();
          String writerUsername = post.writerUsername();
          String receiverName = post.receiverName();
          String receiverUsername = post.receiverUsername();
          JSONObject postInfo = new JSONObject();
          postInfo
                .put("writerId", writerId)
                .put("receiverId", receiverId)
                .put("artist", artist)
                .put("album", album)
                .put("privacy", privacy)
                .put("message", message)
                .put("likes", likes)
//                .put("songId", songId)
                .put("title", title)
                .put("songUrl", songUrl)
                .put("artist", artist)
                .put("albumImageUrl", albumImageUrl)
                .put("dateCreated", dateCreated)
                .put("writerName", writerName)
                .put("writerUsername", writerUsername)
                .put("receiverName", receiverName)
                .put("receiverUsername", receiverUsername);
          result.add(postInfo);
        }
        return result;
    }

    public List<JSONObject> getPublicFeed(final RequestContext requestContext) {
        String username = requestContext.pathArgs().get("username");
        Optional<String> token = requestContext.request().header("session-token");
        String userId = authUtils.verifyToken(token.get());
        List<Post> posts = postStore.getPublicFeed(username);
        List<JSONObject> result = new ArrayList<>();
        if (!posts.isEmpty()) {
            try {
                result = JSONListfromPosts(result, posts);
            } catch (NullPointerException e) {
                throw new NullPointerException();
            }

        }
        LOG.info("result: {}", result);
        return result;
    }

    // Asynchronous Middleware Handling for payloads
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