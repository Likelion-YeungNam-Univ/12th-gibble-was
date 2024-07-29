package gible.global.util.jwt;

import gible.global.common.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class JwtHelper {
    private final JwtTokenProvider accessTokenProvider;
    private final JwtTokenProvider refreshTokenProvider;

    public JwtHelper(
            @Qualifier("accessTokenProvider") JwtTokenProvider accessTokenProvider,
            @Qualifier("refreshTokenProvider") JwtTokenProvider refreshTokenProvider) {
        this.accessTokenProvider = accessTokenProvider;
        this.refreshTokenProvider = refreshTokenProvider;
    }

    public String generateAccessToken(String email, UUID userId, String role) {
        return accessTokenProvider.generateToken(email, userId, role);
    }

    public String generateRefreshToken(String email, UUID userId, String role) {
        return refreshTokenProvider.generateToken(email, userId, role);
    }

    public Claims parseClaims(String token) {
        return accessTokenProvider.parseClaims(token);
    }
}
