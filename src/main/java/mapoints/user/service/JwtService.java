package mapoints.user.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import mapoints.user.model.User;
import mapoints.user.model.UserType;
import mapoints.user.view.JwtView;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {
    public JwtView generateJwt(User user) {
        UserType userType = user.getUserType();
        String entityId = user.getEntityId();

        String jwtId = UUID.randomUUID().toString();
        String jwtToken = JWT.create()
                .withSubject(entityId)
                .withExpiresAt(new Date(System.currentTimeMillis() + getJwtExpiry(userType)))
                .withJWTId(jwtId)
                .sign(Algorithm.HMAC512(getJwtSecret(userType).getBytes()));

        return new JwtView(jwtId, jwtToken);
    }


    public DecodedJWT getDecodedJWT(String jwt, UserType userType) throws JWTVerificationException {
        return JWT.require(Algorithm.HMAC512(getJwtSecret(userType).getBytes()))
                .build()
                .verify(jwt.trim());
    }

    private Long getJwtExpiry(UserType userType){
        return 1000L;
    }

    private String getJwtSecret(UserType userType){
        return "some random jwt key ... ... ";
    }
}
