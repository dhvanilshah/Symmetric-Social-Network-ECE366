package cool.disc.server.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.exceptions.UnirestException;
import cool.disc.server.handler.auth.Authenticate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Album {
    private final ObjectMapper objectMapper;
    List<String> urls = new ArrayList<String>();

    public Album(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<String> getUrls() throws UnirestException {
        try {
            Authenticate auth = new Authenticate(objectMapper);
            urls = auth.getTrackUrl();
        } catch (UnirestException | IOException e) {
            throw new UnirestException(e);
        }
        if (!urls.isEmpty()) {
            for (String url : urls) {
//                System.out.println(url);
                return urls;
            }
        }
        return null;
    }
//
//  public Stream<Route<AsyncHandler<Response<ByteString>>>> routes() {
//    return java.util.stream.Stream.of(
//            Route.sync("GET", "/tracks", this::getUrls).withMiddleware(jsonMiddleware())
//    );
//  }
    // Asynchronous Middleware Handling for payloads
//  private <T> Middleware<AsyncHandler<T>, AsyncHandler<Response<ByteString>>> jsonMiddleware() {
//    return JsonSerializerMiddlewares.<T>jsonSerialize(objectMapper.writer())
//            .and(Middlewares::httpPayloadSemantics)
//            .and(
//                    responseAsyncHandler ->
//                            requestContext ->
//                                    responseAsyncHandler
//                                            .invoke(requestContext)
//                                            .thenApply(
//                                                    response -> response.withHeader("Access-Control-Allow-Origin", "*")));
//  }


    // later
//  public String getName() {
//    return name;
//  }
//
//  public Album getAlbum() {
//    return album;
//  }
}