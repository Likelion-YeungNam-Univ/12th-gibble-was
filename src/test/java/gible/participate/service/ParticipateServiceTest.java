package gible.participate.service;

import gible.domain.event.entity.Event;
import gible.domain.event.repository.EventRepository;
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

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private Event event;

    @Mock
    private Participate participate;

    private String userEmail;
    private UUID eventId;

    @BeforeEach
    void setUp() {
        this.userEmail = "test@gmail.com";
        this.eventId = UUID.randomUUID();
    }

    @Test
    @DisplayName("이벤트 참여 테스트")
    void participationEventTest() {
        // given
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(participateRepository.save(any(Participate.class))).thenReturn(participate);

        // when
        participateService.participationEvent(userEmail, eventId);

        // then
        verify(userRepository, times(1)).findByEmail(userEmail);
        verify(eventRepository, times(1)).findById(eventId);
        verify(participateRepository, times(1)).save(any(Participate.class));
    }

    @Test
    @DisplayName("이벤트 참여 실패 테스트 - 사용자 없음")
    void participationEventFailedUserNotFoundTest() {
        // given
        when(userRepository.findByEmail(userEmail)).thenThrow(new CustomException(ErrorType.USER_NOT_FOUND));

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            participateService.participationEvent(userEmail, eventId);
        });

        // then
        assertEquals(exception.getErrortype(), ErrorType.USER_NOT_FOUND);
        verify(userRepository, times(1)).findByEmail(userEmail);
    }

    @Test
    @DisplayName("이벤트 참여 실패 테스트 - 이벤트 없음")
    void participationEventFailedEventNotFoundTest() {
        // given
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(eventRepository.findById(eventId)).thenThrow(new CustomException(ErrorType.EVENT_NOT_FOUND));

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            participateService.participationEvent(userEmail, eventId);
        });

        // then
        assertEquals(exception.getErrortype(), ErrorType.EVENT_NOT_FOUND);
        verify(userRepository, times(1)).findByEmail(userEmail);
        verify(eventRepository, times(1)).findById(eventId);
    }
}
