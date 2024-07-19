package gible.domain.user.controller;

import gible.domain.event.dto.EventSummaryRes;
import gible.domain.security.common.SecurityUserDetails;
import gible.domain.user.dto.MyPageRes;
import gible.domain.user.dto.SignUpReq;
import gible.domain.user.service.UserService;
import gible.global.util.api.SuccessRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/signUp")
    public ResponseEntity<?> SignUp(@RequestBody  SignUpReq signUpReq){
        userService.signUp(signUpReq);
        return ResponseEntity.ok().body(SuccessRes.from("회원가입 성공"));
    }
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
