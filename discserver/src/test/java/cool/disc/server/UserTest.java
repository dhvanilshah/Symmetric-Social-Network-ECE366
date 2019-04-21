//package cool.disc.server;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import okio.ByteString;
//import com.spotify.apollo.Request;
//import com.spotify.apollo.RequestContext;
//import com.spotify.apollo.Response;
//import com.spotify.apollo.Status;
//import cool.disc.server.handler.user.UserHandlers;
//import cool.disc.server.model.User;
//import cool.disc.server.model.UserBuilder;
//import cool.disc.server.store.user.UserStore;
//import org.junit.After;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.io.IOException;
//import java.util.Optional;
//
//import static org.mockito.Mockito.*;
//
///* Unit test for User Handler */
//@RunWith(MockitoJUnitRunner.class)
//public class UserTest
//{
//
//    /* Need to test addUser, getUser */
//
//    @Mock ObjectMapper objectMapper;
//    @Mock UserStore userStore;
//    @Mock RequestContext requestContext;
//    @Mock ByteString requestPayloadByteString;
//    @Mock Request request;
//
//    private UserHandlers testClass;
//    private User testUser;
//
//    /* Testing getUser */
//    @Before
//    public void setup()
//    {
//        testUser = new UserBuilder()
//                .id("test_id")
//                .username("test_username")
//                .name("test_name")
//                .password("test_password")
//                .service("test_service")
//                .photo("test_photo")
//                .build();
//

//        testClass = new UserHandlers(objectMapper, userStore, authUtils);

//    }
//
//
//    @Test
//    public void addUser() throws IOException {
//        byte[] byteArray = new byte[0];
//        when(requestContext.request()).thenReturn(request);
//        when(request.payload()).thenReturn(Optional.of(requestPayloadByteString));
//        when(requestPayloadByteString.toByteArray()).thenReturn(byteArray);
//        when(objectMapper.readValue(byteArray, User.class)).thenReturn(testUser);
//
//        when(userStore.addUser(testUser)).thenReturn(1);
//
//        Response handlerReturn = testClass.addUser(requestContext);
//
//        Assert.assertEquals(handlerReturn.status(), Status.OK);
//
//
//
//    }
//
//    /*@Test
//    public void getUserTest()
//    {
//        when(store.getUser(null)).thenReturn(null);
//        //Assert.assertEquals(handler.getUser(), null);
//    }*/
//
//    @After
//    public void tearDown()
//    {
//        testUser = null;
//        testClass = null;
//    }
//
//
//
//
//}
//
//
///* request_context.request().payload() */
