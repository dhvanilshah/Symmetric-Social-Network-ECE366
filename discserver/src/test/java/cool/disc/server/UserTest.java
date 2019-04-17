package cool.disc.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import cool.disc.server.utils.AuthUtils;
import okio.ByteString;
import com.spotify.apollo.Request;
import com.spotify.apollo.RequestContext;
import com.spotify.apollo.Response;
import com.spotify.apollo.Status;
import cool.disc.server.handler.user.UserHandlers;
import cool.disc.server.model.User;
import cool.disc.server.model.UserBuilder;
import cool.disc.server.store.user.UserStore;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

/* Unit test for User Handler */
@RunWith(MockitoJUnitRunner.class)
public class UserTest
{

    /* Need to test addUser, getUser */

    @Mock ObjectMapper objectMapper;
    @Mock UserStore userStore;
    @Mock RequestContext requestContext;
    @Mock ByteString requestPayloadByteString;
    @Mock Request request;
    @Mock AuthUtils auth;

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
                .service("test_service")
                .photo("test_photo")
                .build();

        testClass = new UserHandlers(objectMapper, userStore);
    }


    /* This method tests the addUser method in the UserHandlers class */
    @Test
    public void addUser() throws IOException
    {
        /* Mocking the analysis of the request payload */
//        byte[] byteArray = new byte[0];
//        when(requestContext.request()).thenReturn(request);
//        when(request.payload()).thenReturn(Optional.of(requestPayloadByteString));
//        when(requestPayloadByteString.toByteArray()).thenReturn(byteArray);
//        when(objectMapper.readValue(byteArray, User.class)).thenReturn(testUser);

        when(requestContext.pathArgs()).thenReturn(Collections.singletonMap("name", testUser.name()));

        /* Mocking interaction with the DB */
        when(userStore.addUser(testUser)).thenReturn(1);

        /* Getting the response from our handler method */
        Response handlerReturn = testClass.addUser(requestContext);

        /* Making sure the response was OK */
        Assert.assertEquals(handlerReturn.status(), Status.OK);


    }

    @Test
    public void getUser() throws IOException
    {
        /* Mocking analysis of request payload */
        when(requestContext.pathArgs()).thenReturn(Collections.singletonMap("name", testUser.name()));

        /* Mocking interaction with DB */
        when(userStore.getUser(testUser.name())).thenReturn(Collections.singletonList(testUser));

        /* Getting the list from the handler method */
        List <User> handlerReturn = testClass.getUser(requestContext);

        /* Make sure we got the right user back */
        Assert.assertEquals(handlerReturn.get(0), testUser);
    }

/* Needs work */
//    @Test
//    public void addFriend() throws IOException
//    {
//        /* Create a friend user */
//        User friend = new UserBuilder()
//                .id("friend_id")
//                .username("friend_username")
//                .name("friend_name")
//                .password("friend_password")
//                .service("friend_service")
//                .photo("friend_photo")
//                .build();
//
//        /* Mock the request context and request */
//        when(requestContext.pathArgs()).thenReturn(Collections.singletonMap("name", testUser.name()));
//        when(requestContext.request()).thenReturn(request);
//        when(request.header("session-token")).thenReturn(Optional.of("token"));
//
//        /* Mock the authorization */
//        when(auth.verifyToken("token")).thenReturn(testUser.id());
//
//        /* Mocking interaction with DB */
//        when(userStore.addFriend(friend.id(), testUser.id())).thenReturn(testUser.id());
//
//        /* Getting the response from the handler method -- Needs work */
//        Response handlerReturn = testClass.addFriend(requestContext);
//
//        /* Make sure we got the right response back */
//        Assert.assertEquals(handlerReturn.status(), Status.OK);
//
//        /* Make sure we got the right response message back */
//        Assert.assertEquals(handlerReturn.payload(), testUser.id());
//
//    }

    @Test
    public void login()
    {
        /* Mock request */
        when(requestContext.pathArgs()).thenReturn(Collections.singletonMap("name", testUser.name()));
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
    @After
    public void tearDown()
    {
        testUser = null;
        testClass = null;
    }




}


/* request_context.request().payload() */
