//package cool.disc.server;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.protobuf250.ByteString;
//import com.spotify.apollo.Request;
//import com.spotify.apollo.RequestContext;
//import com.spotify.apollo.Response;
//import com.spotify.apollo.Status;
//import cool.disc.server.handler.user.UserHandlers;
//import cool.disc.server.model.User;
//import cool.disc.server.model.UserBuilder;
//import cool.disc.server.store.user.UserStoreController;
//import org.junit.After;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//
//import org.mockito.Mock;
//
//import java.util.Optional;
//
//import static org.mockito.Mockito.*;
//
///* Unit test for User Handler */
//public class UserTest
//{
//
//    /* Need to test addUser, getUser */
//
//    User test_user;
//    UserHandlers handler;
//    ObjectMapper object_mapper;
//
//    @Mock UserStoreController store;   /* Mocks interaction with DB */
//    @Mock Request request;             /* Mocks request from front end */
//    @Mock RequestContext req_context;  /* Mocks request context */
//
//
//    /* Testing getUser */
//    @Before
//    public void setUp()
//    {
//        object_mapper = new ObjectMapper();
//        handler = new UserHandlers(object_mapper, store);
//        test_user = new UserBuilder()
//                .id("test_id")
//                .username("test_username")
//                .name("test_name")
//                .password("test_password")
//                .service("test_service")
//                .photo("test_photo")
//                .build();
//
//        when(req_context.request()).thenReturn(request);
//    }
//
//
//    @Test
//    public void addUserTest()
//    {
//        /*Response response = store.addUser(test_user.username(),
//                test_user.name(),
//                test_user.password(),
//                test_user.service(),
//                test_user.photo());
//        when(response.status().code() == 200).thenReturn(Boolean.TRUE);*/
//
//
//        when(store.addUser(test_user.username(),
//                test_user.name(),
//                test_user.password(),
//                test_user.service(),
//                test_user.photo())).thenReturn(Response.forStatus(Status.OK));
//
//        //when(req_context.request().payload().isPresent()).then(Assert.assertEquals(handler.addUser(req_context).status().code(), 200));
//        Assert.assertEquals(handler.addUser(req_context).status().code(), 200);
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
//        object_mapper = null;
//        handler = null;
//        test_user = null;
//    }
//
//
//
//
//}
//
//
///* request_context.request().payload() */
