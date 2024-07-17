package gible.domain.user.service;

import gible.domain.user.entity.User;
import gible.domain.user.repository.UserRepository;
import gible.exception.CustomException;
import gible.exception.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findById(UUID userId){
        return userRepository.findById(userId).orElseThrow(()-> new CustomException(ErrorType.USER_NOT_FOUND));
    }
}
