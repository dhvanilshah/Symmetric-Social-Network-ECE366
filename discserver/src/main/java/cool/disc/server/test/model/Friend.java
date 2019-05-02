package cool.disc.server.test.model;

import io.norberg.automatter.AutoMatter;
import org.bson.types.ObjectId;

@AutoMatter
public interface Friend {
    ObjectId userId();
    Integer score();
}
