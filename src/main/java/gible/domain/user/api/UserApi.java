package gible.domain.user.api;

import gible.domain.security.common.SecurityUserDetails;
import gible.domain.user.dto.MyPageRes;
import gible.domain.user.dto.SignUpReq;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "[사용자 API]", description = "사용자 관련 API")
public interface UserApi {

    ResponseEntity<?> SignUp(@Valid @RequestBody SignUpReq signUpReq);

    ResponseEntity<MyPageRes> getMyPage(
            @AuthenticationPrincipal SecurityUserDetails userDetails
    );

    ResponseEntity<?> getPostByUserId(@AuthenticationPrincipal SecurityUserDetails userDetails);
}
