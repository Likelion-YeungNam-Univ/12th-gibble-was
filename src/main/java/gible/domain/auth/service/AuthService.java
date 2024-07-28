package gible.domain.auth.service;

import gible.domain.auth.dto.KakaoUserInfo;
import gible.domain.auth.dto.SignInReq;

import gible.domain.auth.dto.SignInRes;
import gible.global.common.jwt.AccessTokenProvider;
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
    private final AccessTokenProvider accessTokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional(readOnly = true)
    public SignInRes login(SignInReq signInReq) {
        //KakaoUserInfo kakaoUserInfo = getUserInfo(signInReq);
        User user = userService.findByEmail("lth8905@naver.com");
        System.out.println(user.getEmail());
        if(user == null) {
            throw new CustomException(ErrorType.NEED_SIGNUP);
        }
        String accessToken = accessTokenProvider.generateToken(user.getEmail(), user.getId(), user.getRole().toString());
        String refreshToken = refreshTokenService.saveRefreshToken(user.getEmail(), user.getId(), user.getRole().toString());

        return SignInRes.of(accessToken, refreshToken);
    }

    @Transactional(readOnly = true)
    public SignInRes reissueToken(String refreshToken){
        if(!refreshTokenService.getRefreshToken(refreshToken)){
            throw new CustomException(ErrorType.TOKEN_EXPIRED);
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
