package gible.event.service;

import gible.domain.event.dto.EventDetailRes;
import gible.domain.event.dto.EventReq;
import gible.domain.event.dto.EventSummaryRes;
import gible.domain.event.entity.Event;
import gible.domain.event.repository.EventRepository;
import gible.domain.event.service.EventService;
import gible.exception.CustomException;
import gible.exception.error.ErrorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    private UUID eventId;

    private Event event1;
    private Event event2;

    @BeforeEach
    void setUp() {
        this.eventId = UUID.randomUUID();

        createEvent();
    }

    private void createEvent() {
        this.event1 = Event.builder()
                .title("제목1")
                .content("내용1")
                .imageUrl("http://qewqeqw.asd")
                .build();

        this.event2 = Event.builder()
                .title("제목2")
                .content("내용2")
                .imageUrl("http://asdasd.zxc")
                .build();
    }

    @Test
    @DisplayName("이벤트 생성하기 테스트")
    void saveEventTest() {
        // given
        EventReq eventReq = new EventReq("이벤트 제목", "이벤트 내용", "http://asdsad.cxz");
        Event event = EventReq.toEntity(eventReq);
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        // when
        eventService.saveEvent(eventReq);

        // then
        assertEquals(event.getTitle(), eventReq.title());
        assertEquals(event.getContent(), eventReq.content());
        assertEquals(event.getImageUrl(), eventReq.imageUrl());
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    @DisplayName("이벤트 목록 조회 테스트")
    void getAllEventTest() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Event> events = new PageImpl<>(List.of(
                event1, event2
        ));

        when(eventRepository.findAll(pageable)).thenReturn(events);

        // when
        Page<EventSummaryRes> eventSummaryPage = eventService.getAllEvents(pageable, null);

        // then
        assertNotNull(eventSummaryPage);
        assertEquals(2, eventSummaryPage.getSize());
        assertEquals(event1.getTitle(), eventSummaryPage.getContent().get(0).title());
        assertEquals(event1.getImageUrl(), eventSummaryPage.getContent().get(0).imageUrl());
        assertEquals(event2.getTitle(), eventSummaryPage.getContent().get(1).title());
        assertEquals(event2.getImageUrl(), eventSummaryPage.getContent().get(1).imageUrl());
    }

    @Test
    @DisplayName("특정 이벤트 조회 테스트")
    void getEventTest() {
        // given
        when(eventRepository.findById(eventId)).thenReturn(Optional.ofNullable(event1));

        // when
        EventDetailRes eventDetailRes = eventService.getEvent(eventId);

        // then
        assertNotNull(eventDetailRes);
        assertEquals(event1.getId(), eventDetailRes.eventId());
        assertEquals(event1.getTitle(), eventDetailRes.title());
        assertEquals(event1.getContent(), eventDetailRes.content());
        assertEquals(event1.getImageUrl(), eventDetailRes.imageUrl());
    }

    @Test
    @DisplayName("특정 이벤트 조회 실패 테스트 - 존재하지 않는 이벤트")
    void getEventFailedEventNotFoundTest() {
        // given
        when(eventRepository.findById(eventId)).thenThrow(new CustomException(ErrorType.EVENT_NOT_FOUND));

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            eventService.getEvent(eventId);
        });

        // then
        assertEquals(ErrorType.EVENT_NOT_FOUND, exception.getErrortype());
        verify(eventRepository, times(1)).findById(eventId);
    }

    @Test
    @DisplayName("이벤트 수정 테스트")
    void updateEventTest() {
        // given
        EventReq updateEvent = new EventReq("제목수정", "내용수정", "http://zxczxc.zxc");

        when(eventRepository.findById(eventId)).thenReturn(Optional.ofNullable(event1));

        // when
        eventService.updateEvent(updateEvent, eventId);

        // then
        assertEquals(updateEvent.title(), event1.getTitle());
        assertEquals(updateEvent.content(), event1.getContent());
        assertEquals(updateEvent.imageUrl(), event1.getImageUrl());
    }

    @Test
    @DisplayName("이벤트 삭제 테스트")
    void deleteEventTest() {
        // given

        // when
        eventService.deleteEvent(eventId);

        // then
        verify(eventRepository, times(1)).deleteById(eventId);
    }
}
