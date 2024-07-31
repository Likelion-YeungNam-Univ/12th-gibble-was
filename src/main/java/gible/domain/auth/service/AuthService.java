package gible.domain.auth.service;

import gible.domain.auth.dto.KakaoUserInfo;
import gible.domain.auth.dto.SignInReq;

import gible.domain.auth.dto.SignInRes;
import gible.domain.user.entity.User;
import gible.domain.user.service.UserService;
import gible.exception.CustomException;
import gible.exception.error.ErrorType;
import gible.global.util.jwt.JwtHelper;
import gible.global.util.cookie.CookieUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final KakaoService kakaoService;
    private final JwtHelper jwtHelper;
    private final RefreshTokenService refreshTokenService;
    private final CookieUtil cookieUtil;
    @Transactional(readOnly = true)
    public ResponseEntity<?> login(SignInReq signInReq) {
        KakaoUserInfo kakaoUserInfo = getUserInfo(signInReq);
        User user = userService.findByEmail(kakaoUserInfo.email());
        if(user == null) {
            return ResponseEntity.status(510).body(kakaoUserInfo);
        }
        String accessToken = jwtHelper.generateAccessToken(user.getEmail(), user.getId(), user.getRole().toString());
        String refreshToken = jwtHelper.generateRefreshToken(user.getEmail(), user.getId(), user.getRole().toString());
        refreshTokenService.saveRefreshToken(user.getId(), refreshToken);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("accessToken", accessToken);

        return ResponseEntity.ok().header("Set-Cookie",
                        cookieUtil.addRtkCookie("refreshToken", refreshToken).toString())
                .body(responseBody);
    }

    @Transactional(readOnly = true)
    public SignInRes reissueToken(String refreshToken){
        if(!refreshTokenService.getRefreshToken(refreshToken)){
            throw new CustomException(ErrorType.TOKEN_NOT_FOUND);
        }
        String newAccessToken = refreshTokenService.reIssueAccessToken(refreshToken);
        String newRefreshToken = refreshTokenService.reIssueRefreshToken(refreshToken);
        return SignInRes.of(newAccessToken, newRefreshToken);
    }

    public void logout(UUID userId) {
        refreshTokenService.deleteRefreshToken(userId.toString());
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

}
