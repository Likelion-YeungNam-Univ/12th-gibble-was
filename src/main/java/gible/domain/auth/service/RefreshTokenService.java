package gible.domain.auth.service;

import gible.domain.security.jwt.JwtTokenProvider;
import gible.global.util.redis.RedisUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RedisUtil redisUtil;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;

    public String saveRefreshToken(String email, UUID userId, String role) {
        String refreshToken = jwtTokenProvider.generateRefreshToken(email, userId, role);

        redisUtil.save(userId.toString(), refreshToken);
        redisUtil.saveExpire(userId.toString(), refreshExpiration);

        return refreshToken;
    }

    public boolean getRefreshToken(String userId) {
        return redisUtil.get(userId);
    }

    public void deleteRefreshToken(String userId) {
        redisUtil.delete(userId);
    }

    public String reIssueAccessToken(String refreshToken) {
        Claims claims = jwtTokenProvider.parseClaims(refreshToken);
        return jwtTokenProvider.generateAccessToken(
                claims.getSubject(),
                UUID.fromString(claims.get("useId", String.class)),
                claims.get("role", String.class)
        );
    }

    public String reIssueRefreshToken(String refreshToken) {
        this.deleteRefreshToken(refreshToken);

        Claims claims = jwtTokenProvider.parseClaims(refreshToken);
        return this.saveRefreshToken(
                claims.getSubject(),
                UUID.fromString(claims.get("useId", String.class)),
                claims.get("role", String.class)
        );
    }
}
