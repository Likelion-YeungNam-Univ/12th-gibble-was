package gible.domain.auth.service;

import gible.domain.auth.dto.SignInReq;
import gible.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final KakaoService kakaoService;

    public String login(SignInReq signInReq) {
        kakaoService.getUserInfo(signInReq);
    }
}
