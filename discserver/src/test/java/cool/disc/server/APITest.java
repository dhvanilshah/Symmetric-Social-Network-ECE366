package cool.disc.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify.apollo.Request;
import com.spotify.apollo.RequestContext;
import com.spotify.apollo.Response;
import cool.disc.server.handler.auth.APIHandlers;
import cool.disc.server.model.Song;
import cool.disc.server.model.SongBuilder;
import cool.disc.server.store.song.SongStore;
import cool.disc.server.utils.AuthUtils;
import okio.ByteString;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class APITest
{
    @Mock ObjectMapper objectMapper;
    @Mock
    SongStore songStore;
    @Mock RequestContext requestContext;
    @Mock ByteString requestPayloadByteString;
    @Mock Request request;
    @Mock
    AuthUtils auth;
    @Mock JsonNode test_node;

    private APIHandlers testClass;
    private Song testSong;

    @Before
    public void setup()
    {
        ObjectId song_id = new ObjectId();


        testSong = new SongBuilder()
            .id(song_id)
            .songId("test_id")
            .title("test_title")
            .songUrl("test_url")
            .artist("test_artist")
            .artistId("test_artist_id")
            .albumName("test_album")
                    .build();

        testClass = new APIHandlers(objectMapper, songStore);

    }

    @Test
    public void addSong() throws IOException
    {
        /* Mock request */
        when(requestContext.request()).thenReturn(request);
        when(request.payload()).thenReturn(Optional.of(requestPayloadByteString));
        when(requestPayloadByteString.utf8()).thenReturn("test");
        when(objectMapper.readTree("test")).thenReturn(test_node);
        when(objectMapper.readValue(test_node.toString(), Song.class)).thenReturn(testSong);

        /* Mock DB */
        when(songStore.addSong(testSong)).thenReturn(Response.ok());

        /* Handler */
        Response <Object> handlerReturn = testClass.addSong(requestContext);

        /* Assert */
        Assert.assertEquals(Response.ok().status(), handlerReturn.status());
    }

}
