package gible.domain.auth.api;

import gible.domain.auth.dto.SignInReq;
import gible.domain.security.common.SecurityUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "인증API")
public interface AuthApi {

    @Operation(summary = "로그인", description = "사용자가 로그인하기 위한 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                        {
                                            "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpX...."
                                        }
                                    """)
                    })),
            @ApiResponse(responseCode = "400", description = "로그인 실패")
    })
    public ResponseEntity<?> login(@Valid @RequestBody SignInReq signInReq);

    @Operation(summary = "로그아웃", description = "사용자가 로그아웃하기 위한 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그아웃 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                        {
                                            "response": "로그아웃 성공"
                                        }
                                    """)
                    })
            )
    })
    public ResponseEntity<?> logout(@AuthenticationPrincipal SecurityUserDetails userDetails);

    @Operation(summary = "액세스토큰 재발급", description = "사용자가 액세스토큰을 재발급 받기 위한 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "액세스토큰 재발급 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                        {
                                            "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpX....",
                                    """)
                    })
            )
    })
    public ResponseEntity<?> reissueToken(@CookieValue("refreshToken") String refreshToken);


    @Operation(summary = "사용자 회원탈퇴", description = "사용자가 회원탈퇴를 하기 위한 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 회원탈퇴 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                        {
                                            "response": "유저삭제 성공"
                                        }
                                    """)
                    })
            )
    })
    public ResponseEntity<?> withdraw(@AuthenticationPrincipal SecurityUserDetails userDetails);
}
