package gible.domain.event.service;

import gible.domain.event.dto.EventDetailRes;
import gible.domain.event.dto.EventReq;
import gible.domain.event.dto.EventSummaryRes;
import gible.domain.event.entity.Event;
import gible.domain.event.repository.EventRepository;
import gible.exception.CustomException;
import gible.exception.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class EventService {

    private final EventRepository eventRepository;

    /* 이벤트 생성 */
    @Transactional
    public void saveEvent(EventReq eventReq) {

        eventRepository.save(EventReq.toEntity(eventReq));
    }

    /* 이벤트 목록 조회 */
    @Transactional(readOnly = true)
    public Page<EventSummaryRes> getAllEvents(Pageable pageable) {

        Page<Event> events = eventRepository.findAll(pageable);
        return events.map(EventSummaryRes::fromEntity);
    }

    /* 특정 이벤트 조회 */
    @Transactional(readOnly = true)
    public EventDetailRes getEvent(UUID eventId) {

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new CustomException(ErrorType.EVENT_NOT_FOUND));
        return EventDetailRes.fromEntity(event);
    }

    /* 이벤트 수정 */
    @Transactional
    public void updateEvent(EventReq updateEventReq, UUID eventId) {

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new CustomException(ErrorType.EVENT_NOT_FOUND));

        event.updateEvent(updateEventReq);
    }

    /* 이벤트 삭제 */
    @Transactional
    public void deleteEvent(UUID eventId) {

        eventRepository.deleteById(eventId);
    }
}
