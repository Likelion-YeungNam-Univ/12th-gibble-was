package gible.domain.user.service;

import gible.domain.user.dto.DupcheckRes;
import gible.domain.user.dto.MyPageRes;
import gible.domain.user.dto.SignUpReq;
import gible.domain.user.dto.UserInfoRes;
import gible.domain.user.entity.User;
import gible.domain.user.repository.UserRepository;
import gible.exception.CustomException;
import gible.exception.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User findById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Transactional(readOnly = true)
    public MyPageRes getMyPage(UUID userId) {
        User user = findById(userId);
        return MyPageRes.fromEntity(user);
    }

    @Transactional
    public UserInfoRes getUserInfo(UUID userId) {
        User user = findById(userId);
        return UserInfoRes.of(user.getName(), user.getPhoneNumber());
    }

    @Transactional
    public void deleteById(UUID userId) {
        userRepository.deleteById(userId);
    }

    @Transactional
    public void signUp(SignUpReq signUpReq) {
        if(userRepository.existsByEmail(signUpReq.email()))
            throw new CustomException(ErrorType.ALREADY_EXISTS_USER);
        userRepository.save(SignUpReq.toEntity(signUpReq));
    }

    @Transactional(readOnly = true)
    public DupcheckRes dupCheck(String nickname) {
        return DupcheckRes.from(userRepository.existsByNickname(nickname));
    }
}
