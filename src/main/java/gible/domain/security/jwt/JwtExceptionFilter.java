package gible.domain.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import gible.exception.error.ErrorType;
import gible.global.common.response.ErrorRes;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");

        log.info("jwtExceptionFilter 실행");
        try{
            chain.doFilter(request, response);
        } catch (ExpiredJwtException e){
            log.info("expiredJwtEcxeption");
            ErrorType errorType = ErrorType.TOKEN_EXPIRED;
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(objectMapper.writeValueAsString(ErrorRes.of(errorType.getStatus(), errorType.getMessage())));
        } catch (JwtException e){
            log.info("JwtException");
            ErrorType errorType = ErrorType.TOKEN_NOT_FOUND;
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write(objectMapper.writeValueAsString(ErrorRes.of(errorType.getStatus(), errorType.getMessage())));
        }
    }
}
