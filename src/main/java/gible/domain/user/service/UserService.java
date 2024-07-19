package gible.domain.user.service;

import gible.domain.event.dto.EventSummaryRes;
import gible.domain.event.repository.EventRepository;
import gible.domain.user.dto.MyPageRes;
import gible.domain.user.entity.User;
import gible.domain.user.repository.UserRepository;
import gible.exception.CustomException;
import gible.exception.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    public User findById(UUID userId){
        return userRepository.findById(userId).orElseThrow(()-> new CustomException(ErrorType.USER_NOT_FOUND));
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email).orElse(null);
    }

    public MyPageRes getMyPage(UUID userId) {
        User user = findById(userId);
        return MyPageRes.of(user.getEmail(), user.getNickname(), user.getPoint());
    }

    public List<EventSummaryRes> getParticipationEvents(UUID userId) {

    }
}
