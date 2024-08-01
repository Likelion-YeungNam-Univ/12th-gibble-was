package gible.domain.auth.controller;


import gible.domain.auth.dto.SignInReq;
import gible.domain.auth.service.AuthService;
import gible.domain.security.common.SecurityUserDetails;
import gible.global.common.response.SuccessRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/kakaologin")
    public ResponseEntity<?> login(@Valid @RequestBody SignInReq signInReq) {
        return authService.login(signInReq);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal SecurityUserDetails userDetails){
        authService.logout(userDetails.getId());
        return ResponseEntity.ok(SuccessRes.from("로그아웃 성공"));
    }

    @PostMapping("/token")
    public ResponseEntity<?> reissueToken(@CookieValue("refreshToken") String refreshToken){
        return ResponseEntity.ok().body(authService.reissueToken(refreshToken));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@AuthenticationPrincipal SecurityUserDetails userDetails) {
        authService.withdraw(userDetails.getId());
        return ResponseEntity.ok(SuccessRes.from("유저삭제 성공"));
    }
}
