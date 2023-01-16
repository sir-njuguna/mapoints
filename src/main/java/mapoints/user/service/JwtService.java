package mapoints.user.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import mapoints.lib.exception.CommonRuntimeException;
import mapoints.lib.exception.ExceptionType;
import mapoints.lib.exception.NotImplementedException;
import mapoints.user.model.User;
import mapoints.user.model.UserType;
import mapoints.user.view.JwtView;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {
    private final Integer TOKEN_VALIDITY_IN_MINUTES = 60;
    private String jwtKeyMerchant;
    private String jwtKeyAdmin;
    private String jwtKeyCustomer;

    private BCryptPasswordEncoder passwordEncoder;
    public JwtView generateJwt(User user) {
        UserType userType = user.getUserType();
        String entityId = user.getEntityId();

        String jwtId = UUID.randomUUID().toString();
        String jwtToken = JWT.create()
                .withSubject(entityId)
                .withExpiresAt(DateUtils.addMinutes(new Date(), TOKEN_VALIDITY_IN_MINUTES))
                .withJWTId(jwtId)
                .sign(Algorithm.HMAC512(getJwtSecret(userType).getBytes()));

        return new JwtView(jwtId, jwtToken);
    }


    public DecodedJWT getDecodedJWT(String jwt, UserType userType) throws JWTVerificationException {
        return JWT.require(Algorithm.HMAC512(getJwtSecret(userType).getBytes()))
                .build()
                .verify(jwt.trim());
    }

    public void  validateJwtId(String tokenJwtId, String savedJwtId){
        if(!passwordEncoder.matches(tokenJwtId, savedJwtId)){
            throw new CommonRuntimeException(ExceptionType.NOT_AUTHORISED, "auth.invalid");
        }
    }


    private String getJwtSecret(UserType userType){
        String jwtKey = null;
        switch (userType){
            case MERCHANT:
                jwtKey = jwtKeyMerchant;
                break;
            case CUSTOMER:
                jwtKey = jwtKeyCustomer;
                break;
            case ADMIN:
                jwtKey = jwtKeyAdmin;
                break;
            default:
                throw new NotImplementedException();
        }
        return jwtKey;
    }


    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Value("${jwt.key.merchant}")
    public void setJwtKeyMerchant(String jwtKeyMerchant) {
        this.jwtKeyMerchant = jwtKeyMerchant;
    }

    @Value("${jwt.key.customer}")
    public void setJwtKeyCustomer(String jwtKeyCustomer) {
        this.jwtKeyCustomer = jwtKeyCustomer;
    }

    @Value("${jwt.key.admin}")
    public void setJwtKeyAdmin(String jwtKeyAdmin) {
        this.jwtKeyAdmin = jwtKeyAdmin;
    }
}
