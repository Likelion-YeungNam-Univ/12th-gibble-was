package gible.domain.user.api;

import gible.domain.security.common.SecurityUserDetails;
import gible.domain.user.dto.MyPageRes;
import gible.domain.user.dto.SignUpReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "[사용자 API]", description = "사용자 관련 API")
public interface UserApi {

    @Operation(summary = "사용자 회원가입", description = "사용자 회원가입을 위한 API")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원가입 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                        {
                                            "response": "회원가입 성공"
                                        }
                                    """)
                    })
            )
    })
    ResponseEntity<?> SignUp(@Valid @RequestBody SignUpReq signUpReq);

    @Operation(summary = "마이페이지 접속", description = "사용자의 마이페이지 접속을 위한 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "마이페이지 접속 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                        {
                                            "email": "abc@gmail.com",
                                            "nickname": "yourNickname"
                                        }
                                    """)
                    })
            )
    })
    ResponseEntity<MyPageRes> getMyPage(
            @AuthenticationPrincipal SecurityUserDetails userDetails
    );

    @Operation(summary = "사용자의 게시글 가져오기", description = "사용자가 올린 게시글을 가져오기 위한 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 게시글 가져오기 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                        [
                                            {
                                                "postId": "845224c-d722-4479-ba8d-a3547f103fad",
                                                "title": "title1"
                                            },
                                            {
                                                "postId": "84ae24c-d722-4479-ba8d-a3547fh28fad",
                                                "title": "title1"
                                            }
                                        ]
                                    """)
                    })
            )
    })
    ResponseEntity<?> getPostByUserId(@AuthenticationPrincipal SecurityUserDetails userDetails);
}
