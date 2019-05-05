package cool.disc.server.store.request;

import io.norberg.automatter.AutoMatter;
import org.bson.types.ObjectId;

import java.util.Date;

@AutoMatter
public interface RequestStore {
    ObjectId getUserId();
    Date getDate();
}
