package gible.domain.auth.api;

import gible.domain.auth.dto.SignInReq;
import gible.domain.security.common.SecurityUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "[인증 API]", description = "인증 관련 API")
public interface AuthApi {

    ResponseEntity<?> login(@Valid @RequestBody SignInReq signInReq);

    ResponseEntity<?> logout(@AuthenticationPrincipal SecurityUserDetails userDetails);

    ResponseEntity<?> reissueToken(@CookieValue("refreshToken") String refreshToken);

    ResponseEntity<?> withdraw(@AuthenticationPrincipal SecurityUserDetails userDetails);

}
