package gible.domain.security.jwt;


import gible.config.JwtConfig;
import gible.exception.CustomException;
import gible.exception.error.ErrorType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final JwtConfig jwtConfig;


    public boolean validateToken(String token) {
        Claims claims = parseClaims(token);
        if(claims == null){
            throw new CustomException(ErrorType.INVALID_TOKEN);
        }
        if(claims.getExpiration().before(new Date())){
            throw new CustomException(ErrorType.TOKEN_EXPIRED);
        }
        return !claims.getExpiration().before(new Date());
    }

    public String generateAccessToken(String email, UUID userId, String role) {
        Map<String, Object> claims = createClaims(userId, role);
        return createToken(email, claims, jwtConfig.getAccessExpiration());
    }

    public String generateRefreshToken(String email, UUID userId, String role) {
        Map<String, Object> claims = createClaims(userId, role);
        return createToken(email, claims, jwtConfig.getRefreshExpiration());
    }

    public String createToken(String email, Map<String, Object> claims, Long expiration) {
        return Jwts.builder()
                .subject(email)
                .issuer(jwtConfig.getIssuer())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .claims(claims)
                .signWith(jwtConfig.getSecretKey())
                .compact();
    }

    public Claims parseClaims(String token){
        return Jwts.parser()
                .verifyWith(jwtConfig.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private static Map<String, Object> createClaims(UUID userId, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", role);
        return claims;
    }
}
