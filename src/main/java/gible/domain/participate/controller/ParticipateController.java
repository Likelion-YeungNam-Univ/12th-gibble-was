package gible.domain.participate.controller;

import gible.domain.participate.dto.ParticipationEventRes;
import gible.domain.participate.service.ParticipateService;
import gible.domain.security.common.SecurityUserDetails;
import gible.global.util.api.ApiUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class ParticipateController {

    private final ParticipateService participateService;

    /* 이벤트 참여 */
    @PostMapping("/event/{eventId}/participation")
    public ResponseEntity<?> participationEvent(@AuthenticationPrincipal SecurityUserDetails userDetails,
                                                @PathVariable UUID eventId) {

        participateService.participationEvent(userDetails.getId(), eventId);
        return ResponseEntity.ok(ApiUtil.from("참여 완료."));
    }

    /* 사용자가 참여한 이벤트 목록 조회하기 */
    @GetMapping("/my-page/participation-event")
    public List<ParticipationEventRes> getAllParticipationEvents(
            @AuthenticationPrincipal SecurityUserDetails userDetails) {

        return participateService.getAllParticipationEvents(userDetails.getId());
    }
}

