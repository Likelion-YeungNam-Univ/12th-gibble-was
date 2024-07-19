package gible.domain.auth.controller;


import gible.domain.auth.dto.RenewTokenReq;
import gible.domain.auth.dto.SignInReq;
import gible.domain.auth.dto.SignInRes;
import gible.domain.auth.service.AuthService;
import gible.domain.auth.util.CookieUtil;
import gible.domain.security.common.SecurityUserDetails;
import gible.global.util.api.SuccessRes;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/kakaologin")
    public ResponseEntity<?> login(@Valid @RequestBody SignInReq signInReq, HttpServletResponse response) {
        SignInRes signInRes = authService.login(signInReq);
        CookieUtil.addRtkCookie(response, signInRes.refreshToken());

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("accessToken", signInRes.accessToken());

        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal SecurityUserDetails userDetails){
        authService.logout(userDetails.getId());
        return ResponseEntity.ok(SuccessRes.from("로그아웃 성공"));
    }

    @PostMapping("/token")
    public ResponseEntity<?> renewToken(@Valid @RequestBody RenewTokenReq renewTokenReq) {
        return ResponseEntity.ok().body(authService.reIssueToken(renewTokenReq));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@AuthenticationPrincipal SecurityUserDetails userDetails) {
        authService.withdraw(userDetails.getId());
        return ResponseEntity.ok(SuccessRes.from("유저삭제 성공"));
    }
}
