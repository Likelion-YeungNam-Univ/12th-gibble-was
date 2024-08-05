package gible.domain.participate.controller;

import gible.domain.participate.api.ParticipateApi;
import gible.domain.participate.service.ParticipateService;
import gible.domain.security.common.SecurityUserDetails;
import gible.global.common.response.SuccessRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class ParticipateController implements ParticipateApi {

    private final ParticipateService participateService;

    /* 이벤트 참여 */
    @Override
    @PostMapping("/event/{eventId}/participation")
    public ResponseEntity<?> participationEvent(@AuthenticationPrincipal SecurityUserDetails userDetails,
                                                @PathVariable UUID eventId) {

        participateService.participationEvent(userDetails.getId(), eventId);
        return ResponseEntity.ok(SuccessRes.from("참여 완료."));
    }

    /* 사용자가 참여한 이벤트 목록 조회하기 */
    @Override
    @GetMapping("/user/participation-event")
    public ResponseEntity<?> getAllParticipationEvents(
            @AuthenticationPrincipal SecurityUserDetails userDetails) {

        return ResponseEntity.ok().body(participateService.getAllParticipationEvents(userDetails.getId()));
    }
}

