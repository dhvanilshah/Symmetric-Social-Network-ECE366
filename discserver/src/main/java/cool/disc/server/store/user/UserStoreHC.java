//package cool.disc.server.store;
//
//import cool.disc.server.model.User;
//import cool.disc.server.model.UserBuilder;
//import org.bson.types.ObjectId;
//
//public class UserStoreHC implements UserStore {
//
//    public UserStoreHC(){
//
//    }
//
//    @Override
//    public User addUser(final String username, final String firstName, final String lastName, final String password, final String service, final String photo) {
//       String UID = "507f1f77bcf86cd799439011";
//        return new UserBuilder()
//                .id(UID)
//                .username(username)
//                .firstName(firstName)
//                .lastName(lastName)
//                .password(password)
//                .service(service)
//                .photo(photo)
//                .build();
//    }
//}
