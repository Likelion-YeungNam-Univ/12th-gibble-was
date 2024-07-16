package gible.domain.security.jwt;


import gible.domain.user.service.UserService;
import gible.exception.CustomException;
import gible.exception.error.ErrorType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenProvider {
    private final SecretKey secretKey;
    private final long expiration;
    private final String issuer;
    private final UserService userService;
    public JwtTokenProvider(
    @Value("${jwt.secret}") final String SECERT_KEY,
    @Value("${jwt.expiration}") final long EXPIRATION,
    @Value("${jwt.issuer}") final String ISSUER,
    final UserService userService
    ){
        this.secretKey = new SecretKeySpec(SECERT_KEY.getBytes(), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.expiration = EXPIRATION;
        this.issuer = ISSUER;
        this.userService = userService;
    }


    public boolean validateToken(String token) {
        Claims claims = parseClaims(token);

        if(userService.findById(claims.get("auth", UUID.class)) == null){
            throw new CustomException(ErrorType.USER_NOT_FOUND);
        }
        if(!claims.getExpiration().before(new Date())){
            return true;
        }
        return false;
    }

    public Authentication getAuthentication(String token) {
        
    }

    private Claims parseClaims(String accessToken){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(accessToken)
                .getPayload();
    }
}
