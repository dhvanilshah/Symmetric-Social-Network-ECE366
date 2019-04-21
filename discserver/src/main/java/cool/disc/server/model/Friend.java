package cool.disc.server.model;

import io.norberg.automatter.AutoMatter;
import org.bson.types.ObjectId;

@AutoMatter
public interface Friend {
    ObjectId userId();
    Integer score();
}
