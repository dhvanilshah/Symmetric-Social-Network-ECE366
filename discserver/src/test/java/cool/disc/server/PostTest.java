package cool.disc.server;

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
        testClass = new PostHandlers(objectMapper, postStore, userStore);
    }

    @Test
    public void getAllPost()
    {
        /* Create another post */
        ObjectId post_id2 = new ObjectId();
        ObjectId writer_id2 = new ObjectId();
        ObjectId receiver_id2 = new ObjectId();
        ObjectId song_id2 = new ObjectId();

        Post testPost2 = new PostBuilder()
                .id(post_id2)
                .writerId(writer_id2)
                .receiverId(receiver_id2)
                .message("message2")
                .privacy(0)
                .likes(3)
                .songId(song_id2)
                .comments("comment2")
                .build();

        /* Create list of the posts */
        List<Post> post_list = new ArrayList<Post>();
        post_list.add(testPost);
        post_list.add(testPost2);

        /* Mock DB interaction */
        when(postStore.getAllPosts()).thenReturn(post_list);

        /* Get what handler returns */
        List <Post> handler_return = testClass.geAllPosts(requestContext);

        /* Assert */
        Assert.assertEquals(post_list, handler_return);
    }


    @Test
    public void getFeed()
    {
        /* Mock getting a user's id */
        when(userStore.getUserId("test_name")).thenReturn(testPost.id());

        /* Mock DB interaction */
        when(postStore.getFeed("test_name")).thenReturn(Collections.singletonList(testPost));

        /* Mock request */
        when(requestContext.request()).thenReturn(request);
        when(request.parameters()).thenReturn(Collections.singletonMap("name", Collections.singletonList("test_name")));

        /* Test handler */
        List <Post> handler_return = testClass.getFeed(requestContext);

        /* Assert */
        Assert.assertEquals(testPost, handler_return.get(0));

    }

    @Test
    public void addPost() throws IOException
    {
        /* Mock request */
        byte[] byteArray = new byte[0];
        when(requestContext.request()).thenReturn(request);
        when(request.payload()).thenReturn(Optional.of(requestPayloadByteString));
        when(requestPayloadByteString.toByteArray()).thenReturn(byteArray);
        when(objectMapper.readValue(byteArray, Post.class)).thenReturn(testPost);

        /* Mock DB interaction */
        when(postStore.addPost(testPost)).thenReturn(Response.ok());

        /* Check handler */
        Integer hander_return = testClass.addPost(requestContext);

        /* Assert */
        Assert.assertEquals(Status.OK.code(), (long)hander_return);
    }
}