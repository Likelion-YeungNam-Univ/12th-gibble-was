package gible.domain.auth.controller;


import gible.domain.auth.dto.SignInReq;
import gible.domain.auth.dto.SignInRes;
import gible.domain.auth.service.AuthService;
import gible.global.util.cookie.CookieUtil;
import gible.domain.security.common.SecurityUserDetails;
import gible.global.util.api.SuccessRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final CookieUtil cookieUtil;

    @PostMapping("/kakaologin")
    public ResponseEntity<?> login(@Valid @RequestBody SignInReq signInReq) {
        SignInRes signInRes = authService.login(signInReq);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("accessToken", signInRes.accessToken());

        return ResponseEntity.ok().header("Set-Cookie",
                cookieUtil.addRtkCookie("refreshToken", signInRes.refreshToken()).toString())
                .body(responseBody);
    }

    @PostMapping("logout")
    public ResponseEntity<?> logout(){
        authService.logout();
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
