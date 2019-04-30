package cool.disc.server.handler.post;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify.apollo.RequestContext;
import com.spotify.apollo.Response;
import com.spotify.apollo.route.*;
import cool.disc.server.model.Post;
import cool.disc.server.store.post.PostStore;
import cool.disc.server.store.user.UserStore;
import okio.ByteString;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class PostHandlers {
    private static final Logger LOG = LoggerFactory.getLogger(PostHandlers.class);

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
                Route.sync("GET", "/getMyFeed/<name>", this::getMyFeed).withMiddleware(jsonMiddleware()),
//                Route.sync("OPTIONS", "/getMyFeed", rc -> "ok").withMiddleware(jsonMiddleware()),
                Route.sync("GET", "/getPublicFeed/<name>", this::getPublicFeed).withMiddleware(jsonMiddleware()),
//                Route.sync("OPTIONS", "/getPublicFeed", rc -> "ok").withMiddleware(jsonMiddleware()),
                Route.sync("POST", "/addPost", this::addPost).withMiddleware(jsonMiddleware()),
//                Route.sync("OPTIONS", "/addPost", rc -> "ok").withMiddleware(jsonMiddleware()),
                Route.sync("GET", "/getAllPosts", this::getAllPosts).withMiddleware(jsonMiddleware()),
                Route.sync("OPTIONS", "/getAllPosts", rc -> "ok").withMiddleware(jsonMiddleware())
        );
    }

    // retrieves all posts in the database

   public List<JSONObject> getAllPosts(final RequestContext requestContext) {
       List<JSONObject> result = new ArrayList<>();
       List<Post> posts = postStore.getAllPosts();
       result = JSONListfromPosts(result, posts);
       return result;
    }

    // adding a post from parameters received through http request
    // parameters: writerName (not id), receiverName (not id), message
    public Integer addPost(final RequestContext requestContext) {
        Post post;
        JsonNode postVal;
        if (requestContext.request().payload().isPresent()) {
            try {
                postVal = objectMapper.readTree(requestContext.request().payload().get().utf8());
                post = objectMapper.readValue(postVal.toString(), Post.class);
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
  public List<JSONObject> getMyFeed(final RequestContext requestContext) {
        String name = requestContext.pathArgs().get("name");
      List<Post> posts = postStore.getMyFeed(name);
      List<JSONObject> result = new ArrayList<>();
      LOG.info("breakpoint");
      if (!posts.isEmpty()) {
          try {
            result = JSONListfromPosts(result, posts);
            } catch (NullPointerException e) {
              LOG.error("null pointer exception: {}", e.getMessage());
              }
          }
        if (result == null)
            LOG.info("result: {}", result);
        return result;
    }

    private List<JSONObject> JSONListfromPosts(List<JSONObject> result, List<Post> posts) {
        for (Post post : posts) {
          String writerId = post.writerId().toString();
          String receiverId = post.receiverId().toString();
          String artist = post.receiverId().toString();
          String album = post.message();
//          Integer url = post.privacy();
//          Integer likes = post.likes();
//          String songId = post.songId().toString();
//          List<String> comments = post.comments();
          //            LOG.info("title, artist, album, url: {},{},{},{}",name,artist,album,url);
          JSONObject postInfo = new JSONObject();
          postInfo
              .put("writerId", writerId)
              .put("receiverId", receiverId)
              .put("artist", artist)
              .put("album", album);
//              .put("url", url)
//              .put("ilkes", likes)
//              .put("songId", songId)
//              .put("comments", comments);
          result.add(postInfo);

        }
        return result;
    }

    public List<JSONObject> getPublicFeed(final RequestContext requestContext) {
        String name = requestContext.pathArgs().get("name");
        List<JSONObject> result = new ArrayList<>();
        List<Post> posts = postStore.getPublicFeed(name);
        if (posts.size() != 0) {
            result = JSONListfromPosts(result, posts);

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