package gible.domain.event.controller;

import gible.domain.event.dto.EventReq;
import gible.domain.event.entity.Region;
import gible.domain.event.service.EventService;
import gible.global.common.response.SuccessRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/event")
@RestController
public class
EventController {

    private final EventService eventService;

    /* 이벤트 등록 */
    @PostMapping("/upload")
    public ResponseEntity<?> saveEvent(@Valid @RequestBody EventReq eventReq) {

        eventService.saveEvent(eventReq);
        return ResponseEntity.created(null).body(SuccessRes.from("이벤트 작성 성공."));
    }

    /* 이벤트 목록 조회 */
    @GetMapping
    public ResponseEntity<?> getAllEvents(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC, size = 12) Pageable pageable,
            @RequestParam(name = "region", required = false) Region region) {

        if (region == null)
            return ResponseEntity.ok().body(eventService.getAllEvents(pageable));
        return ResponseEntity.ok().body(eventService.getAllEventsByRegion(region, pageable));
    }

    /* 특정 이벤트 조회 */
    @GetMapping("/{eventId}")
    public ResponseEntity<?> getEvent(@PathVariable UUID eventId) {

        return ResponseEntity.ok().body(eventService.getEvent(eventId));
    }

    /* 이벤트 수정 */
    @PutMapping("/upload/{eventId}")
    public ResponseEntity<?> updateEvent(@Valid @RequestBody EventReq updateEventReq,
                                         @PathVariable UUID eventId) {

        eventService.updateEvent(updateEventReq, eventId);
        return ResponseEntity.ok(SuccessRes.from("이벤트 수정 성공."));
    }

    /* 이벤트 삭제 */
    @DeleteMapping("/{eventId}")
    public ResponseEntity<?> deleteEvent(@PathVariable UUID eventId) {

        eventService.deleteEvent(eventId);
        return ResponseEntity.ok(SuccessRes.from("이벤트 삭제 성공."));
    }
}
