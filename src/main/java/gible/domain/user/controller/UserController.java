package gible.domain.user.controller;

import gible.domain.event.dto.EventSummaryRes;
import gible.domain.security.common.SecurityUserDetails;
import gible.domain.user.dto.MyPageRes;
import gible.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<MyPageRes> getMyPage(
            @AuthenticationPrincipal SecurityUserDetails userDetails
    ){
        return ResponseEntity.ok().body(userService.getMyPage(userDetails.getId()));
    }

    @GetMapping("/participation-event")
    public ResponseEntity<List<EventSummaryRes>> getParticipationEvent(
            @AuthenticationPrincipal SecurityUserDetails userDetails
    ){
        return ResponseEntity.ok().body(userService.getParticipationEvents(userDetails.getId()));
    }
}
