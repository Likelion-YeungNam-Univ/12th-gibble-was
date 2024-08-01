package gible.domain.auth.service;

import gible.global.util.jwt.JwtHelper;
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
    private final JwtHelper jwtHelper;

    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;

    public void saveRefreshToken(UUID userId, String refreshToken) {
        redisUtil.save(userId.toString(), refreshToken);
        redisUtil.saveExpire(userId.toString(), refreshExpiration);
    }

    public boolean getRefreshToken(String token) {
        Claims claims = jwtHelper.parseClaims(token);
        return redisUtil.get(claims.get("userId", String.class));
    }

    public void deleteRefreshToken(String userId) {
        redisUtil.delete(userId);
    }

    public String reIssueAccessToken(String refreshToken) {
        Claims claims = jwtHelper.parseClaims(refreshToken);
        return jwtHelper.generateAccessToken(
                claims.getSubject(),
                UUID.fromString(claims.get("userId", String.class)),
                claims.get("role", String.class)
        );
    }

    public String reIssueRefreshToken(String refreshToken) {
        this.deleteRefreshToken(refreshToken);

        Claims claims = jwtHelper.parseClaims(refreshToken);
        UUID userId = UUID.fromString(claims.get("userId", String.class));

        String newRefreshToken = jwtHelper.generateRefreshToken(
                claims.getSubject(),
                userId,
                claims.get("role", String.class)
        );

        this.saveRefreshToken(userId, newRefreshToken);
        return newRefreshToken;
    }
}
