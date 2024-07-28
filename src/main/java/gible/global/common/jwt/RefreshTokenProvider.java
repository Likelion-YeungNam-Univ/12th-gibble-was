package gible.global.common.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Qualifier("refreshTokenProvider")
public class RefreshTokenProvider implements JwtTokenProvider {
    private final SecretKey secretKey;
    private final Duration refreshTokenDuration;
    private final String issuer;

    public RefreshTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.refresh-expiration}") Duration refreshTokenExpiration,
            @Value("${jwt.issuer}") String issuer
    ){
        this.secretKey = new SecretKeySpec(secretKey.getBytes(), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.refreshTokenDuration = refreshTokenExpiration;
        this.issuer = issuer;
    }

    @Override
    public String generateToken(String email, UUID userId, String role) {
        return Jwts.builder()
                .subject(email)
                .issuer(issuer)
                .expiration(createExpire(refreshTokenDuration.toMillis()))
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
