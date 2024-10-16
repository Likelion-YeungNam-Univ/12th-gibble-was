package gible.domain.participate.service;

import gible.domain.event.entity.Event;
import gible.domain.event.repository.EventRepository;
import gible.domain.participate.dto.ParticipationEventRes;
import gible.domain.participate.entity.Participate;
import gible.domain.participate.repository.ParticipateRepository;
import gible.domain.user.entity.User;
import gible.domain.user.repository.UserRepository;
import gible.exception.CustomException;
import gible.exception.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ParticipateService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ParticipateRepository participateRepository;

    /* 이벤트 참여 */
    @Transactional
    public void participationEvent(UUID userId, UUID eventId) {

        if (participateRepository.existsByUser_IdAndEvent_Id(userId, eventId)) {
            throw new CustomException(ErrorType.ALREADY_PARTICIPATE_POST);
        }

        User foundUser = userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ErrorType.USER_NOT_FOUND));

        Event foundEvent = eventRepository.findById(eventId).orElseThrow(() ->
                new CustomException(ErrorType.EVENT_NOT_FOUND));

        participateRepository.save(Participate.builder()
                .user(foundUser)
                .event(foundEvent)
                .build());
    }

    /* 참여한 이벤트 조회 */
    @Transactional(readOnly = true)
    public List<ParticipationEventRes> getAllParticipationEvents(UUID userId) {

        return participateRepository.findByUser_Id(userId)
                .stream().map(ParticipationEventRes::fromEntity)
                .toList();
    }
}
