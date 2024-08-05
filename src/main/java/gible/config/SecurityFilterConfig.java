package gible.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import gible.domain.security.jwt.JwtAuthenticationFilter;
import gible.domain.security.jwt.JwtExceptionFilter;
import gible.global.util.jwt.JwtHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@RequiredArgsConstructor
@Configuration
public class SecurityFilterConfig {
    private final JwtHelper jwtHelper;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    @Bean
    public JwtExceptionFilter jwtExceptionFilter(){
        return new JwtExceptionFilter(objectMapper);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter(jwtHelper, userDetailsService);
    }
}
