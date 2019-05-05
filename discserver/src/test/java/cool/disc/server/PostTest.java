package cool.disc.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify.apollo.Request;
import com.spotify.apollo.RequestContext;
import com.spotify.apollo.Response;
import com.spotify.apollo.Status;
import cool.disc.server.handler.post.PostHandlers;
import cool.disc.server.model.Post;
import cool.disc.server.model.PostBuilder;
import cool.disc.server.model.User;
import cool.disc.server.store.post.PostStore;
import cool.disc.server.store.user.UserStore;
import cool.disc.server.utils.AuthUtils;
import okio.ByteString;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.*;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PostTest
{
    @Mock ObjectMapper objectMapper;
    @Mock PostStore postStore;
    @Mock UserStore userStore;
    @Mock User user;
    @Mock RequestContext requestContext;
    @Mock Request request;
    @Mock AuthUtils auth;
    @Mock ByteString requestPayloadByteString;
    @Mock JsonNode test_node;

    Post testPost;
    private PostHandlers testClass;

    @Before
    public void setUp()
    {
        /* Create a test post with various fields */
        ObjectId post_id = new ObjectId();
        ObjectId writer_id = new ObjectId();
        ObjectId receiver_id = new ObjectId();
        ObjectId song_id = new ObjectId();

        testPost = new PostBuilder()
                .id(post_id)
                .writerId(writer_id)
                .receiverId(receiver_id)
                .message("message")
                .privacy(1)
                .likes(5)
                .songId(song_id)
                .comments("comment")
                .build();

        /* Initialize Class */
        testClass = new PostHandlers(objectMapper, postStore, userStore, auth);
    }

    @Test
    public void addPost() throws IOException
    {
        /* Mock request */
        byte[] byteArray = new byte[0];
        when(requestContext.request()).thenReturn(request);
        when(request.header("session-token")).thenReturn(Optional.of("token"));
        when(auth.verifyToken("token")).thenReturn("writer_id");

        when(request.payload()).thenReturn(Optional.of(requestPayloadByteString));
        when(requestPayloadByteString.utf8()).thenReturn("test");
        when(objectMapper.readTree("test")).thenReturn(test_node);
        when(objectMapper.readValue(test_node.toString(), Post.class)).thenReturn(testPost);


        /* Mock DB interaction */
        when(postStore.addPost(testPost, "writer_id")).thenReturn(Response.ok());

        /* Check handler */
        Integer handler_return = testClass.addPost(requestContext);

        /* Assert */
        Assert.assertEquals(Status.OK.code(), (long)handler_return);
    }
}
