package gible.domain.auth.service;

import gible.domain.auth.dto.KakaoUserInfo;
import gible.domain.auth.dto.RenewTokenReq;
import gible.domain.auth.dto.SignInReq;

import gible.domain.auth.dto.SignInRes;
import gible.domain.security.jwt.JwtTokenProvider;
import gible.domain.user.entity.User;
import gible.domain.user.service.UserService;
import gible.exception.CustomException;
import gible.exception.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final KakaoService kakaoService;
    private final JwtTokenProvider jwtTokenProvider;
    public SignInRes login(SignInReq signInReq) {
        KakaoUserInfo kakaoUserInfo = getUserInfo(signInReq);
        User user = userService.findByEmail(kakaoUserInfo.email());
        if(user == null) {
            throw new CustomException(ErrorType.NEED_SIGNUP);
        }
        return generateSignInRes(user);
    }

    public SignInRes renewToken(RenewTokenReq renewTokenReq){
        UUID uuid = UUID.randomUUID(); //레디스 로직수정필요
        User user = userService.findById(uuid); //레디스 로직수정필요
        return generateSignInRes(user);
    }

    private KakaoUserInfo getUserInfo(SignInReq signInReq) {
        String accessToken = kakaoService.getAccessToken(signInReq);
        return kakaoService.getUserInfo(accessToken);
    }

    private SignInRes generateSignInRes(User user){
        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        return SignInRes.of(accessToken, refreshToken);
    }
}
