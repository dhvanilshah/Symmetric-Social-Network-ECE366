package cool.disc.server.test.store.request;

import io.norberg.automatter.AutoMatter;
import org.bson.types.ObjectId;

import java.util.Date;

@AutoMatter
public interface RequestStore {
    ObjectId getUserId();
    Date getDate();
}
