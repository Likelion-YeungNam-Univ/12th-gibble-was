package gible.domain.user.service;

import gible.domain.event.dto.EventSummaryRes;
import gible.domain.participate.entity.Participate;
import gible.domain.participate.repository.ParticipateRepository;
import gible.domain.user.dto.MyPageRes;
import gible.domain.user.entity.User;
import gible.domain.user.repository.UserRepository;
import gible.exception.CustomException;
import gible.exception.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ParticipateRepository participateRepository;

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
        return MyPageRes.of(user.getEmail(), user.getNickname(), user.getPoint());
    }

    @Transactional(readOnly = true)
    public List<EventSummaryRes> getParticipationEvents(UUID userId) {
        return participateRepository.findByUser_Id(userId)
                .stream().map(Participate::getEvent).toList()
                .stream().map(EventSummaryRes::fromEntity).toList();
    }

    @Transactional
    public void deleteById(UUID userId) {
        userRepository.deleteById(userId);
    }
}
