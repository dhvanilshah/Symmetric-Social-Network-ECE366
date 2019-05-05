package cool.disc.server.model;

import io.norberg.automatter.AutoMatter;

import java.util.Date;

@AutoMatter
public interface Request {
    String userId();
    Date date();
}
