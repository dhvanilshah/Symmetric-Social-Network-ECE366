package cool.disc.server.store.comment;

import cool.disc.server.model.Comment;
import io.norberg.automatter.AutoMatter;
import org.bson.types.ObjectId;

import java.util.Date;

@AutoMatter
public interface CommentStore {
    Comment addComment();
    Comment deleteComment();
    ObjectId getId();
    ObjectId getWriterId();
    ObjectId getPostId();
    Date getDate();
    Integer getLikes();
}
