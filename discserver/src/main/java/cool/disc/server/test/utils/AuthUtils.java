package cool.disc.server.test.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.Map;

public class AuthUtils {

    private final Config config;
    private final String secret;

    public AuthUtils(){
        this.config = ConfigFactory.load("discserver.conf");
        this.secret = this.config.getString("secrets.jwt-key");
    }

    public String verifyToken(String token){
        String uid = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            Map<String, Claim> claims = jwt.getClaims();    //Key is the Claim name
            uid = claims.get("id").asString();
        } catch (JWTVerificationException exception){
            return null;
        }

        return uid;
    }

    public String createToken(String uid){
        String token;
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secret);
            token = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("id", uid)
                    .sign(algorithm);
        } catch (JWTCreationException exception){
           return "Error: Invalid Token";
        }
        return token;
    }
}
