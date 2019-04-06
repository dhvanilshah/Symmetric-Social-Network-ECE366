package cool.disc.server.model;

import io.norberg.automatter.AutoMatter;
import org.bson.types.ObjectId;

import javax.annotation.Nullable;

@AutoMatter
public interface Friend {
    ObjectId userId();
    @Nullable Integer score();
}
