package gible.domain.security.jwt;


import gible.config.JwtConfig;
import gible.domain.user.entity.User;
import gible.domain.user.service.UserService;
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
    private final UserService userService;


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
        return createToken(user.getEmail(), claims, jwtConfig.getAccessExpiration());
    }

    public String generateRefreshToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("role", user.getRole());
        return createToken(user.getEmail(), claims, jwtConfig.getRefreshExpiration());
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
}
