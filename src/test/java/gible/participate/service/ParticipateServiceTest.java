package gible.participate.service;

import gible.domain.event.entity.Event;
import gible.domain.event.repository.EventRepository;
import gible.domain.participate.dto.ParticipationEventRes;
import gible.domain.participate.entity.Participate;
import gible.domain.participate.repository.ParticipateRepository;
import gible.domain.participate.service.ParticipateService;
import gible.domain.user.entity.User;
import gible.domain.user.repository.UserRepository;
import gible.exception.CustomException;
import gible.exception.error.ErrorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParticipateServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ParticipateRepository participateRepository;

    @InjectMocks
    private ParticipateService participateService;

    @Mock
    private User user;

    @Mock
    private Participate participate;

    private UUID userId;
    private UUID eventId;

    private Event event1;
    private Event event2;
    private Participate participate1;
    private Participate participate2;

    @BeforeEach
    void setUp() {
        this.userId = UUID.randomUUID();
        this.eventId = UUID.randomUUID();

        createParticipate();
    }

    private void createParticipate() {
        this.event1 = Event.builder()
                .title("이벤트1")
                .content("내용1")
                .build();

        this.event2 = Event.builder()
                .title("이벤트2")
                .content("내용2")
                .build();

        this.participate1 = Participate.builder()
                .user(user)
                .event(event1)
                .build();

        this.participate2 = Participate.builder()
                .user(user)
                .event(event2)
                .build();
    }

    @Test
    @DisplayName("이벤트 참여 테스트")
    void participationEventTest() {
        // given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event1));
        when(participateRepository.save(any(Participate.class))).thenReturn(participate);

        // when
        participateService.participationEvent(userId, eventId);

        // then
        verify(userRepository, times(1)).findById(userId);
        verify(eventRepository, times(1)).findById(eventId);
        verify(participateRepository, times(1)).save(any(Participate.class));
    }

    @Test
    @DisplayName("이벤트 참여 실패 테스트 - 사용자 없음")
    void participationEventFailedUserNotFoundTest() {
        // given
        when(userRepository.findById(userId)).thenThrow(new CustomException(ErrorType.USER_NOT_FOUND));

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            participateService.participationEvent(userId, eventId);
        });

        // then
        assertEquals(exception.getErrortype(), ErrorType.USER_NOT_FOUND);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("이벤트 참여 실패 테스트 - 이벤트 없음")
    void participationEventFailedEventNotFoundTest() {
        // given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(eventRepository.findById(eventId)).thenThrow(new CustomException(ErrorType.EVENT_NOT_FOUND));

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            participateService.participationEvent(userId, eventId);
        });

        // then
        assertEquals(exception.getErrortype(), ErrorType.EVENT_NOT_FOUND);
        verify(userRepository, times(1)).findById(userId);
        verify(eventRepository, times(1)).findById(eventId);
    }

    @Test
    @DisplayName("참여한 이벤트 조회 테스트")
    void getAllParticipationEventsTest() {
        // given
        List<Participate> participates = List.of(
                participate1, participate2
        );

        when(participateRepository.findByUser_Id(userId)).thenReturn(participates);

        // when
        List<ParticipationEventRes> participationEventList =
                participateService.getAllParticipationEvents(userId);

        // then
        assertNotNull(participationEventList);
        assertEquals(2, participationEventList.size());
        assertEquals(event1.getId(), participationEventList.get(0).event().eventId());
        assertEquals(event1.getTitle(), participationEventList.get(0).event().title());
        assertEquals(event2.getId(), participationEventList.get(1).event().eventId());
        assertEquals(event2.getTitle(), participationEventList.get(1).event().title());
    }
}
