package cool.disc.server.model;

import io.norberg.automatter.AutoMatter;
import org.bson.types.ObjectId;

import javax.annotation.Nullable;
import java.util.List;

@AutoMatter
public interface Post {
    ObjectId id();
    ObjectId writerId();
    ObjectId receiverId();
    String message();
    @Nullable Integer privacy();
    @Nullable Integer likes();
    @Nullable ObjectId songId();
    @Nullable List<String> comments();
}