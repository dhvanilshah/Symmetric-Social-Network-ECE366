package cool.disc.server.test.model;

import io.norberg.automatter.AutoMatter;

import java.util.Date;

@AutoMatter
public interface Request {
    String userId();
    Date date();
}
