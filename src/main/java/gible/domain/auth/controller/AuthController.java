package gible.domain.auth.controller;


import gible.domain.auth.dto.SignInReq;
import gible.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> login(@Valid @RequestBody SignInReq signInReq) {
        return ResponseEntity.ok().body(authService.login(signInReq));
    }

}
