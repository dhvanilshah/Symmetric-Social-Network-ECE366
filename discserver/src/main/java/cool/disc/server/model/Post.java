package cool.disc.server.model;

import io.norberg.automatter.AutoMatter;
import org.bson.types.ObjectId;

import javax.annotation.Nullable;
import java.util.List;

@AutoMatter
public interface Post {
    @Nullable ObjectId id();
    @Nullable ObjectId writerId();
    @Nullable ObjectId receiverId();
    @Nullable String receiverIdString();
    String message();
    @Nullable Integer privacy();
    @Nullable Integer likes();
    @Nullable ObjectId songId();
    @Nullable String songIdString();
    @Nullable List<String> comments();
}