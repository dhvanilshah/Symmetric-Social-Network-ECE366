package cool.disc.server.model;

import io.norberg.automatter.AutoMatter;
import org.bson.types.ObjectId;

import javax.annotation.Nullable;
import java.util.List;

@AutoMatter
public interface Post {
    // change all id fields to be required?
    @Nullable ObjectId id();
    @Nullable ObjectId writerId();
    @Nullable ObjectId receiverId();
    @Nullable String receiverIdString();
    String message();
    // privacy, likes will be 0 for default, so not nullable?
    @Nullable Integer privacy();
    @Nullable Integer likes();
    // a post should include a song, so songid should not be nullable?
    @Nullable ObjectId songId();
    // songIdStirng can come from songId, so unnecesary?
//    @Nullable String songIdString();
    @Nullable List<String> comments();
}