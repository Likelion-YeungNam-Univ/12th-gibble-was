package gible.domain.event.api;

import gible.domain.event.dto.EventReq;
import gible.domain.event.entity.Region;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Tag(name = "[이벤트 API]", description = "이벤트 관련 API")
public interface EventApi {

    ResponseEntity<?> saveEvent(@Valid @RequestBody EventReq eventReq);

    ResponseEntity<?> getAllEvents(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC, size = 12) Pageable pageable,
            @RequestParam(name = "region", required = false) Region region);

    ResponseEntity<?> getEvent(@PathVariable UUID eventId);

    ResponseEntity<?> updateEvent(@Valid @RequestBody EventReq updateEventReq,
                                  @PathVariable UUID eventId);

    ResponseEntity<?> deleteEvent(@PathVariable UUID eventId);
}
