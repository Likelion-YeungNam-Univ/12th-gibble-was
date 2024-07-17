package gible.domain.auth.service;

import gible.domain.auth.dto.KakaoUserInfo;
import gible.domain.auth.dto.SignInReq;

import gible.domain.security.jwt.JwtTokenProvider;
import gible.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final KakaoService kakaoService;
    private final JwtTokenProvider jwtTokenProvider;
    public String login(SignInReq signInReq) {
        KakaoUserInfo kakaoUserInfo = getUserInfo(signInReq);
        if (!userService.isExist(kakaoUserInfo.email())){

        }
    }

    private KakaoUserInfo getUserInfo(SignInReq signInReq) {
        String accessToken = kakaoService.getAccessToken(signInReq);
        return kakaoService.getUserInfo(accessToken);
    }
}
