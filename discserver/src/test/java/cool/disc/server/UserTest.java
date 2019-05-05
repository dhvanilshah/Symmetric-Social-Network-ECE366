package cool.disc.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cool.disc.server.handler.user.UserHandlers;
import cool.disc.server.utils.AuthUtils;
import okio.ByteString;
import com.spotify.apollo.Request;
import com.spotify.apollo.RequestContext;
import com.spotify.apollo.Response;
import com.spotify.apollo.Status;
import cool.disc.server.model.User;
import cool.disc.server.model.UserBuilder;
import cool.disc.server.store.user.UserStore;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.io.IOException;
import java.util.*;

import static org.mockito.Mockito.*;

/* Unit test for User Handler */
@RunWith(MockitoJUnitRunner.class)
public class UserTest
{
    @Mock ObjectMapper objectMapper;
    @Mock UserStore userStore;
    @Mock RequestContext requestContext;
    @Mock ByteString requestPayloadByteString;
    @Mock Request request;
    @Mock AuthUtils auth;
    @Mock JsonNode test_node;

    private UserHandlers testClass;
    private User testUser;

    @Before
    public void setup()
    {
        testUser = new UserBuilder()
                .id("test_id")
                .username("test_username")
                .name("test_name")
                .password("test_password")
                .photo("test_photo")
                .build();

        testClass = new UserHandlers(objectMapper, userStore, auth);
    }


    /* This method tests the addUser method in the UserHandlers class */
    @Test
    public void addUser() throws IOException
    {
        /* Mocking the analysis of the request payload */
        byte[] byteArray = new byte[0];
        when(requestContext.request()).thenReturn(request);
        when(request.payload()).thenReturn(Optional.of(requestPayloadByteString));
        when(requestPayloadByteString.utf8()).thenReturn("test");
        when(objectMapper.readTree("test")).thenReturn(test_node);
        when(objectMapper.readValue(test_node.toString(), User.class)).thenReturn(testUser);

//        when(requestPayloadByteString.toByteArray()).thenReturn(byteArray);
//        when(objectMapper.readValue(byteArray, User.class)).thenReturn(testUser);

        //when(requestContext.pathArgs()).thenReturn(Collections.singletonMap("name", testUser.name()));

        /* Mocking interaction with the DB */
        when(userStore.addUser(testUser)).thenReturn(1);

        /* Getting the response from our handler method */
        Response handlerReturn = testClass.addUser(requestContext);

        /* Making sure the response was OK */
        Assert.assertEquals(handlerReturn.status(), Status.OK);


    }

    @Test
    public void getUser()
    {
        /* Mocking analysis of request payload */
        when(requestContext.request()).thenReturn(request);
        //when(request.payload()).thenReturn(Optional.of(requestPayloadByteString));
        when(request.header("session-token")).thenReturn(Optional.of("token"));
        when(auth.verifyToken("token")).thenReturn("user_id");
        when(requestContext.pathArgs()).thenReturn(Collections.singletonMap("name", testUser.name()));
        when(userStore.getUser(testUser.name(), "user_id")).thenReturn(Collections.singletonList(testUser));

        /* Getting the list from the handler method */
        Response<List<User>> handlerReturn = testClass.getUser(requestContext);

        /* Make sure we got the right user back */
        Assert.assertEquals(handlerReturn.status(), Status.OK);
        Assert.assertEquals(handlerReturn.payload(), Optional.of(Collections.singletonList(testUser)));
    }

    @Test
    public void login()
    {
        /* Mock request */
        when(requestContext.request()).thenReturn(request);
        when(request.parameter("username")).thenReturn(Optional.of(testUser.username()));
        when(request.parameter("password")).thenReturn(Optional.of(testUser.password()));

        /* Mock DB interaction */
        when(userStore.login(testUser.username(), testUser.password())).thenReturn("valid");

        /* Getting the response from the handler method -- Needs work */
        Response handlerReturn = testClass.login(requestContext);

        /* Assert both the status and the payload */
        Assert.assertEquals(handlerReturn.status(), Status.OK);
        Assert.assertEquals(handlerReturn.payload(), Optional.of("valid"));
    }

    @Test
    public void addFriend()
    {
        /* Create a friend user */
        User friend = new UserBuilder()
                .id("friend_id")
                .username("friend_username")
                .name("friend_name")
                .password("friend_password")
                .photo("friend_photo")
                .build();

        /* Mock the request context and request */
        when(requestContext.pathArgs()).thenReturn(Collections.singletonMap("id", friend.id()));
        when(requestContext.request()).thenReturn(request);
        when(request.header("session-token")).thenReturn(Optional.of("token"));

        /* Mock the authorization */
        when(auth.verifyToken("token")).thenReturn(testUser.id());

        /* Mocking interaction with DB */
        when(userStore.addFriend(friend.id(), testUser.id())).thenReturn(testUser.id());

        /* Getting the response from the handler method */
        Response handlerReturn = testClass.addFriend(requestContext);

        /* Make sure we got the right response back */
        Assert.assertEquals(handlerReturn.status(), Status.OK);
    }

    @Test
    public void acceptRequest()
    {
        /* Create a friend user */
        User friend = new UserBuilder()
                .id("friend_id")
                .username("friend_username")
                .name("friend_name")
                .password("friend_password")
                .photo("friend_photo")
                .build();

        /* Mock the request context and request */
        when(requestContext.pathArgs()).thenReturn(Collections.singletonMap("id", friend.id()));
        when(requestContext.request()).thenReturn(request);
        when(request.header("session-token")).thenReturn(Optional.of("token"));
        when(auth.verifyToken("token")).thenReturn(testUser.id());


        /* Call the handler */
        Response handlerReturn = testClass.acceptRequest(requestContext);

        /* Assert */
        Assert.assertEquals(handlerReturn.status(), Status.OK);

    }

    @Test
    public void getRequest()
    {
        /* Mock the request context and request */
        when(requestContext.request()).thenReturn(request);
        when(request.header("session-token")).thenReturn(Optional.of("token"));
        when(auth.verifyToken("token")).thenReturn(testUser.id());

        /* Mock the DB */
        when(userStore.getRequests(testUser.id())).thenReturn(Collections.singletonList(testUser));

        /* Test the handler */
        Response<List<User>> handlerReturn = testClass.getRequests(requestContext);

        /* Assert */
        Assert.assertEquals(handlerReturn.status(), Status.OK);
        Assert.assertEquals(handlerReturn.payload(), Optional.of(Collections.singletonList(testUser)));
    }

    @Test
    public void getFriends()
    {
        /* Mock the request context and request */
        when(requestContext.request()).thenReturn(request);
        when(request.header("session-token")).thenReturn(Optional.of("token"));
        when(auth.verifyToken("token")).thenReturn(testUser.id());

        /* Mock the DB */
        when(userStore.getRequests(testUser.id())).thenReturn(Collections.singletonList(testUser));

        /* Test the handler */
        Response<List<User>> handlerReturn = testClass.getRequests(requestContext);

        /* Assert */
        Assert.assertEquals(handlerReturn.status(), Status.OK);
        Assert.assertEquals(handlerReturn.payload(), Optional.of(Collections.singletonList(testUser)));
    }

    @Test
    public void getBio()
    {
        /* Mock the request context and request */
        when(requestContext.request()).thenReturn(request);
        when(request.header("session-token")).thenReturn(Optional.of("token"));
        when(auth.verifyToken("token")).thenReturn(testUser.id());
        when(requestContext.pathArgs()).thenReturn(Collections.singletonMap("username", testUser.username()));

        /* Mock DB */
        when(userStore.getBio(testUser.id(), testUser.username())).thenReturn(testUser);

        /* Handler */
        Response<User> handlerReturn = testClass.getBio(requestContext);

        /* Assert */
        Assert.assertEquals(handlerReturn.status(), Status.OK);
        Assert.assertEquals(handlerReturn.payload(), Optional.of(testUser));
    }

    @Test
    public void updateBio() throws IOException
    {
        /* Mock the request context and request */
        when(requestContext.request()).thenReturn(request);
        when(request.header("session-token")).thenReturn(Optional.of("token"));
        when(auth.verifyToken("token")).thenReturn(testUser.id());

        /* Handler */
        Response<User> handlerReturn = testClass.getBio(requestContext);

        /* Assert */
        Assert.assertEquals(handlerReturn.status(), Status.OK);
    }
}
