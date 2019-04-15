package cool.disc.server.handler.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import cool.disc.server.store.user.UserStore;

public class AuthHandlers {
    private final ObjectMapper objectMapper;
    private final UserStore userStore;

    public AuthHandlers(final ObjectMapper objectMapper, final UserStore userStore){
        this.objectMapper = objectMapper;
        this.userStore = userStore;
    }

}
