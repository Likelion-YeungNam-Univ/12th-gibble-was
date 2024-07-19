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
import jakarta.servlet.http.HttpServletRequest;
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
        return generateSignInRes(user);
    }

    @Transactional(readOnly = true)
    public SignInRes reIssueToken(RenewTokenReq renewTokenReq){
        UUID uuid = UUID.randomUUID(); //레디스 로직수정필요
        User user = userService.findById(uuid); //레디스 로직수정필요
        return generateSignInRes(user);
    }

    public void logout(UUID userId) {
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

    private SignInRes generateSignInRes(User user){
        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        return SignInRes.of(accessToken, refreshToken);
    }

}
