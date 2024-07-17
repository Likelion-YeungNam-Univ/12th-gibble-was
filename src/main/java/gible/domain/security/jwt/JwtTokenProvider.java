package gible.domain.security.jwt;


import gible.domain.user.entity.User;
import gible.domain.user.service.UserService;
import gible.exception.CustomException;
import gible.exception.error.ErrorType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtTokenProvider {
    private final SecretKey secretKey;
    private final long accessExpiration;
    private final long refreshExpiration;
    private final String issuer;
    private final UserService userService;

    public JwtTokenProvider(
    @Value("${jwt.secret}") final String SECERT_KEY,
    @Value("${jwt.access-expiration}") final long ACCESS_EXPIRATION,
    @Value("${jwt.refresh-expiration}") final long REFRESH_EXPIRATION,
    @Value("${jwt.issuer}") final String ISSUER,
    final UserService userService
    ){
        this.secretKey = new SecretKeySpec(SECERT_KEY.getBytes(), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.accessExpiration = ACCESS_EXPIRATION;
        this.refreshExpiration = REFRESH_EXPIRATION;
        this.issuer = ISSUER;
        this.userService = userService;
    }


    public boolean validateToken(String token) {
        Claims claims = parseClaims(token);
        if(userService.findById(claims.get("auth", UUID.class)) == null){
            throw new CustomException(ErrorType.USER_NOT_FOUND);
        }
        return !claims.getExpiration().before(new Date());
    }

    public String generateAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("role", user.getRole());
        return createToken(user.getEmail(), claims, accessExpiration);
    }

    public String generateRefreshToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("role", user.getRole());
        return createToken(user.getEmail(), claims, refreshExpiration);
    }

    public String createToken(String email, Map<String, Object> claims, Long expiration) {
        return Jwts.builder()
                .subject(email)
                .issuer(issuer)
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .claims(claims)
                .signWith(secretKey)
                .compact();
    }

    protected Claims parseClaims(String accessToken){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(accessToken)
                .getPayload();
    }
}
