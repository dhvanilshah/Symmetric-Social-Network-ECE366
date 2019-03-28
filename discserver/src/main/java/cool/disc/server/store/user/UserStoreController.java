package cool.disc.server.store.user;

import com.mongodb.client.*;
import cool.disc.server.model.User;
import cool.disc.server.model.UserBuilder;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class UserStoreController implements UserStore {

    private MongoClient mongoClient = MongoClients.create();
    private MongoDatabase database = mongoClient.getDatabase("discbase");
    private MongoCollection<Document> userCollection = database.getCollection("users");

    public UserStoreController() {}

    @Override
    public User addUser(final String username,
                        final String name,
                        final String password,
                        final String service,
                        final String photo){

        Document newUser = new Document("name", name)
                .append("username", username)
                .append("password", password)
                .append("service", service)
                .append("photo", photo);

        userCollection.insertOne(newUser);
        ObjectId id = (ObjectId)newUser.get( "_id" );

             return new UserBuilder()
                     .id(id.toHexString())
                     .username(username)
                     .name(name)
                     .password(password)
                     .service(service)
                     .photo(photo)
                     .build();
    }

    @Override
    public List<User> getUser(final String name){
        System.out.println("hello - query");
        Document regQuery = new Document();
        regQuery.append("$regex", "^(?)" + Pattern.quote(name));
        regQuery.append("$options", "i");

        Document findQuery = new Document();
        findQuery.append("name", regQuery);
        FindIterable<Document> iterable = userCollection.find(findQuery);

        List<User> usrlist = new ArrayList<User>();
        for(Document userDoc : iterable){
            ObjectId id = (ObjectId)userDoc.get( "_id" );
            String nameusr = userDoc.getString("name");
            String username = userDoc.getString("username");
            User user = new UserBuilder()
                    .id(id.toHexString())
                    .username(username)
                    .name(nameusr)
                    .build();
            usrlist.add(user);
        }
        return usrlist;
    }
}
