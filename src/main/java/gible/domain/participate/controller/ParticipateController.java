package gible.domain.participate.controller;

import gible.domain.participate.service.ParticipateService;
import gible.global.util.api.ApiUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class ParticipateController {

    private final ParticipateService participateService;

    /* 이벤트 참여 */
    @PostMapping("/event/{eventId}/participation")
    public ResponseEntity<?> participationEvent(@AuthenticationPrincipal UserDetails userDetails,
                                                @PathVariable UUID eventId) {

        participateService.participationEvent(userDetails.getUsername(), eventId);
        return ResponseEntity.ok(ApiUtil.from("참여 완료."));
    }
}

