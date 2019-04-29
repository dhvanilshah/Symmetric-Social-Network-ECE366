package cool.disc.server.store.song;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.spotify.apollo.Response;
import com.spotify.apollo.Status;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import cool.disc.server.model.Song;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

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
//        database = dbClient.getDatabase(databaseString);
//        songCollection = database.getCollection(this.config.getString("mongo.collection_song"));

        MongoClient dbClient = new MongoClient( "localhost" , 27017 );
        database = dbClient.getDatabase("discbase");
        songCollection = database.getCollection("songs");
    }

    @Override
    public Response<Object> addSong(Song newSong){
// DONT ADD THE SONG IF IT ALREADY EXISTS
        try {
            Document song = songCollection.find(eq("songUrl", newSong.songUrl())).first();
            String songId = song.get("_id").toString();
            return Response.ok().withPayload(songId);
        } catch (Exception e) {
            e.printStackTrace();
        }

// IF THE SONG DOESNT EXIST, ADD IT TO THE DB
        Document addSongDoc = new Document()
                .append("title", newSong.title())
                .append("songUrl", newSong.songUrl())
                .append("artist", newSong.artist())
//                .append("albumName", newSong.albumName())
                .append("albumImageUrl", newSong.albumImageUrl())
                .append("score", 0);
        try {
            songCollection.insertOne(addSongDoc);
            Document song = songCollection.find(eq("songUrl", newSong.songUrl())).first();
            String songId = song.get("_id").toString();
            return Response.ok().withPayload(songId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Response<Object> response = getObjectResponse(addSongDoc, songCollection);
//        LOG.info("response: {}", response);
        return response;
    }

    private Response<Object> getObjectResponse(Document addSongDoc, MongoCollection<Document> songCollection) {
        try {
            BasicDBObject songCheck = new BasicDBObject().append("title", addSongDoc.get("title"));
            FindIterable<Document> cursor = songCollection.find(songCheck);
            if (!cursor.iterator().hasNext()) {
                songCollection.insertOne(addSongDoc);
                return Response.ok();
            } else if(cursor.iterator().hasNext()) {
                // update the song's score (+1)
                Document searchedSong = new Document().append("title",addSongDoc.get("title"));
                Bson queriedSong = songCollection.find(searchedSong).iterator().next();
                Integer newScore = ((Document) queriedSong).getInteger("score") + 1;
                Bson scoreUpdateDoc = new Document().append("score",newScore);
                Bson updateOperationDocument = new Document("$set", scoreUpdateDoc);
                songCollection.updateOne(queriedSong, updateOperationDocument);
                return Response.forStatus(Status.FOUND);
            }
        } catch (MongoWriteException e) {
            LOG.info("error: {}",e.getMessage());
        }
        return null;
    }
}
