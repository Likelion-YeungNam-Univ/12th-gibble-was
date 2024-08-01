package gible.global.common.jwt;

import io.jsonwebtoken.Claims;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public interface JwtTokenProvider {
    default Date createExpire(Long expiration){
        return new Date(new Date().getTime() + expiration);
    }

    String generateToken(String email, UUID userId, String role);

    Claims parseClaims(String token);

    Map<String, Object> createClaims(UUID userId, String role);
}
