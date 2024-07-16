package gible.domain.security.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
    public boolean validateToken(String token) {

    }

    public Authentication getAuthentication(String token) {

    }
}
