package gible.domain.auth.controller;


import gible.domain.auth.dto.RenewTokenReq;
import gible.domain.auth.dto.SignInReq;
import gible.domain.auth.dto.SignInRes;
import gible.domain.auth.service.AuthService;
import gible.domain.security.common.SecurityUserDetails;
import gible.global.util.api.SuccessRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/kakaologin")
    public ResponseEntity<SignInRes> login(@Valid @RequestBody SignInReq signInReq) {
        return ResponseEntity.ok().body(authService.login(signInReq));
    }

    @PostMapping("logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal SecurityUserDetails userDetails){
        authService.logout(userDetails.getId());
        return ResponseEntity.ok().body(SuccessRes.from("로그아웃 성공"));
    }
    @PostMapping("/token")
    public ResponseEntity<?> renewToken(@Valid @RequestBody RenewTokenReq renewTokenReq) {
        return ResponseEntity.ok().body(authService.renewToken(renewTokenReq));
    }

}
