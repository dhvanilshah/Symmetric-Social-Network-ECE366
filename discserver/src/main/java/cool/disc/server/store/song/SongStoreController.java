package cool.disc.server.store.song;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientException;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.spotify.apollo.Response;
import com.spotify.apollo.Status;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import cool.disc.server.model.Song;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SongStoreController implements SongStore {
    private static final Logger LOG = LoggerFactory.getLogger(SongStoreController.class);
    private final Config config;

    MongoClientURI uri;
    private MongoClient dbClient;
    private MongoDatabase database;
    private MongoCollection<Document> songCollection;

    public SongStoreController() {
        this.config = ConfigFactory.load("discserver.conf");

        // get login info from config
        String uri1 = this.config.getString("mongo.uri");
        String username = this.config.getString("mongo.username");
        String password = this.config.getString("mongo.password");
        String host = this.config.getString("mongo.host");
        String host2 = this.config.getString("mongo.host2");
        String host3 = this.config.getString("mongo.host3");
        String uriString = uri1 + username + password;

        // initialize db driver
        uri = new MongoClientURI(uriString+host);
        try {
            dbClient = new MongoClient(uri);
        } catch (MongoClientException e) {
            try {
                uri = new MongoClientURI(uriString+host2);
                dbClient = new MongoClient(uri);
            } catch (Exception error) {
                uri = new MongoClientURI(uriString+host3);
                dbClient = new MongoClient(uri);

            }
        }
        String databaseString = this.config.getString("mongo.database");
        database = dbClient.getDatabase(databaseString);
        songCollection = database.getCollection(this.config.getString("mongo.collection_song"));
    }

    public Response<Object> addSong(Song newSong){
        Document addSongDoc = new Document()
                .append("songId", newSong.songId())
                .append("title", newSong.title())
                .append("songUrl", newSong.songUrl())
                .append("artist", newSong.artist())
                .append("artistId", newSong.artistId())
                .append("albumName", newSong.albumName())
                .append("albumImageUrl", newSong.albumImageUrl())
                .append("score", newSong.score());
        Response<Object> response = getObjectResponse(addSongDoc, songCollection);
        LOG.info("response: {}", response);
        return response;
    }
    private Response<Object> getObjectResponse(Document addSongDoc, MongoCollection<Document> songCollection) {
        try {
            songCollection.insertOne(addSongDoc);
            return Response.ok();
        } catch (MongoWriteException e) {
            // todo: update the song's score (+1)
            Bson queriedSong = songCollection.find(addSongDoc).iterator().next();
            Integer newScore = ((Document) queriedSong).getInteger("score") + 1;
            Bson scoreUpdateDoc = new Document().append("score",newScore);
            Bson updateOperationDocument = new Document("$set", scoreUpdateDoc);
            songCollection.updateOne(queriedSong, updateOperationDocument);
            LOG.info("error: {}",e.getMessage());
            return Response.forStatus(Status.CONFLICT);
        }
    }

    public Song getSong(String title) {
        Song result = null;
        Document queryFriends = new Document("title", title);


        return result;
    }

}
