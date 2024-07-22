package gible.domain.auth.service;

import gible.domain.auth.dto.KakaoUserInfo;
import gible.domain.auth.dto.SignInReq;

import gible.domain.auth.dto.SignInRes;
import gible.domain.security.jwt.JwtTokenProvider;
import gible.domain.user.entity.User;
import gible.domain.user.service.UserService;
import gible.exception.CustomException;
import gible.exception.error.ErrorType;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final KakaoService kakaoService;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional(readOnly = true)
    public SignInRes login(SignInReq signInReq) {
        KakaoUserInfo kakaoUserInfo = getUserInfo(signInReq);
        User user = userService.findByEmail(kakaoUserInfo.email());
        if(user == null) {
            throw new CustomException(ErrorType.NEED_SIGNUP);
        }
        return generateSignInRes(user.getEmail(), user.getId(), user.getRole().toString());
    }

    @Transactional(readOnly = true)
    public SignInRes reissueToken(String refreshToken){
        Claims claims = jwtTokenProvider.parseClaims(refreshToken);
        return generateSignInRes(
                claims.getSubject(),
                UUID.fromString(claims.get("userId", String.class)),
                claims.get("role", String.class)
        );
    }

    public void logout() {

        //레디스 리프레시토큰 삭제 로직
    }

    @Transactional
    public void withdraw(UUID userId) {
        userService.deleteById(userId);
        //파이어베이스 연동시 사진 삭제 로직 필요
    }

    private KakaoUserInfo getUserInfo(SignInReq signInReq) {
        String accessToken = kakaoService.getAccessToken(signInReq);
        return kakaoService.getUserInfo(accessToken);
    }

    private SignInRes generateSignInRes(String email, UUID userId, String role){
        String accessToken = jwtTokenProvider.generateAccessToken(email, userId, role);
        String refreshToken = jwtTokenProvider.generateRefreshToken(email, userId, role);

        return SignInRes.of(accessToken, refreshToken);
    }

}
