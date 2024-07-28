package gible.global.common.jwt;


import gible.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class AccessTokenProvider implements JwtTokenProvider {
    private final SecretKey secretKey;
    private final Duration accessTokenDuration;
    private final String issuer;

    public AccessTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access-expiration}") Duration accessTokenExpiration,
            @Value("${jwt.issuer}") String issuer
    ){
        this.secretKey = new SecretKeySpec(secretKey.getBytes(), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.accessTokenDuration = accessTokenExpiration;
        this.issuer = issuer;
    }

    @Override
    public String generateToken(String email, UUID userId, String role) {
        return Jwts.builder()
                .subject(email)
                .issuer(issuer)
                .expiration(createExpire(accessTokenDuration.toMillis()))
                .claims(createClaims(userId, role))
                .signWith(secretKey)
                .compact();
    }

    @Override
    public Claims parseClaims(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @Override
    public Map<String, Object> createClaims(UUID userId, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", role);
        return claims;
    }
}
