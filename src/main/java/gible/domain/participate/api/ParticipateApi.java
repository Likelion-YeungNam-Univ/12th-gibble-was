package gible.domain.participate.api;

import gible.domain.security.common.SecurityUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Tag(name = "[이벤트 참여 API]", description = "이벤트 참여 관련 API")
public interface ParticipateApi {

    ResponseEntity<?> participationEvent(@AuthenticationPrincipal SecurityUserDetails userDetails,
                                         @PathVariable UUID eventId);

    ResponseEntity<?> getAllParticipationEvents(
            @AuthenticationPrincipal SecurityUserDetails userDetails);
}
