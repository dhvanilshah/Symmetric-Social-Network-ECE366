package cool.disc.server.handler.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Authenticate {
    private static final Logger LOG = LoggerFactory.getLogger(Authenticate.class);

    private final ObjectMapper objectMapper;
    private final ObjectWriter objectWriter;
    private final Config config;

    List urls = new ArrayList<String>();
    String access_token = "";

    // constructor: loads token upon initialization
    public Authenticate(ObjectMapper objectMapper) throws IOException, UnirestException {
        this.objectMapper = objectMapper;
        this.objectWriter = objectMapper.writer();
        this.config = ConfigFactory.load("discserver.conf");

        // credentials, options
        String clientId = this.config.getString("auth.client_id");
        String clientSecret = this.config.getString("auth.client_secret");
        String CLIENT_ID_SECRET = clientId + ":" + clientSecret;
        String encodedString = Base64.getEncoder().encodeToString(CLIENT_ID_SECRET.getBytes());
        String opt = this.config.getString("auth.type_option");
        String option = URLEncoder.encode(opt, "utf-8");

        // request for token
        HttpResponse<JsonNode> tokenRequest = Unirest.post("https://accounts.spotify.com/api/token")
                .header("Accept", "application/json")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Basic " + encodedString)
                .body("grant_type=client_credentials".getBytes()).asJson();
        JSONObject body = tokenRequest.getBody().getObject();
        access_token = body.getString("access_token");
        LOG.info("Success on access token: {}", access_token);

    }

    // queries for a search
    public JSONObject search(String query, String type) throws UnirestException, IOException{
        // access Spotify Web API using the token
        // type: album, track, artist, etc.
        // query: search string
        String q = null;
        for(int i = 0; i < query.length(); i++) {
            q = URLEncoder.encode(query, "UTF-8").replace("+", "%20");
        }
        HttpResponse<JsonNode> accessRequest
                // sample urls:
                //       "https://api.spotify.com/v1/tracks/2TpxZ7JUBn3uw46aR7qd6V"
                //       "https://api.spotify.com/v1/search?q=tania%20bowra&type=artist"
            = Unirest.get("https://api.spotify.com/v1/search?" + "q=" + q + "&" + "type=" + type)
                .header("Accept", "application/json")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Bearer " + access_token)
                .asJson();
        JSONObject result=  accessRequest.getBody().getObject();
        return result;
    }
    
    public List<String> getTrackUrl() {
        return urls;
    }

    private static String readStreamToString(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader((inputStream)));
        StringBuilder sb = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
            sb.append(output);
        }
        return sb.toString();
    }
}


// oauth ref: https://collab.ucsd.edu/api/api-documentation/information-for-api-consumers/code-examples/java-example

// custom API on auth0
//            HttpResponse<String> response = Unirest.post("https://dev-3c319w1a.auth0.com/oauth/token")
//                    .header("content-type", "application/json")
//                    .body("{\"grant_type\":\"client_credentials\",\"client_id\": \"Iwayq1OdMkvEnRtKQDc2tCqLP7BbnDPs\",\"client_secret\": \"HkV24nAklI0LZqeki-V1Kal5U4tPPueNyi1EZgH8h0_AE6zP1Pt6vYQzsTyer1Va\",\"audience\": \"https://api.spotify.com/\"}")
//                    .asString();
//            String header = response.getBody().substring(16,137);
//            String body = response.getBody().substring(138,438);
//            String signature = response.getBody().substring(439,781);
//            String jwt = header+"."+body+"."+signature;
//            String jwt = response.getBody().substring(17,781);
//            System.out.println("jwt: " + jwt);
//            HttpResponse<String> certificateResponse = Unirest.get("https://dev-3c319w1a.auth0.com/.well-known/jwks.json")
//                    .header("content-type", "application/json")
//                    .asString();
//            String publicKey = (String) certificateResponse.getBody().subSequence(56, 1095);
//            System.out.println("publicKey: " + publicKey);
