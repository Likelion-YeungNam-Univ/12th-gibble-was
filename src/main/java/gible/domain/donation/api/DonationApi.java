package gible.domain.donation.api;

import gible.domain.donation.dto.DonationReq;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Tag(name = "[기부 API]", description = "기부 관련 API")
public interface DonationApi {

    @Operation(summary = "기부하기", description = "사용자가 기부를 하기 위한 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "기부 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                        {
                                            "response": "기부 성공."
                                        }
                                    """)
                    })),
    })
    ResponseEntity<?> donate(@Valid @RequestBody DonationReq donationReq,
                             @PathVariable UUID postId,
                             @AuthenticationPrincipal SecurityUserDetails userDetails);

    @Operation(summary = "게시글 기부자 목록 불러오기", description = "게시글 기부자 목록을 조회하기 위한 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 기부자 리스트 조회하기 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                        [
                                            {
                                                "nickname": "기부자1",
                                                "donateCount": 5
                                            },
                                            {
                                                "nickname": "기부자2",
                                                "donateCount": 10
                                            }
                                        ]
                                    """)
                    })
            )
    })
    ResponseEntity<?> getDonorsForPost(@PathVariable UUID postId);

    @Operation(summary = "기부한 게시글에 대한 정보 불러오기", description = "기부한 게시글에 대한 조회하기 위한 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "기부한 게시글 리스트 조회하기 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                        [
                                            {
                                                "title": "기부 요청 글 1",
                                                "nickname": "작성자 1"
                                            },
                                            {
                                                "title": "기부 요청 글 2",
                                                "nickname": "작성자 2"
                                            }
                                        ]
                                    """)
                    })
            )
    })
    ResponseEntity<?> getPostDonationDetails(
            @AuthenticationPrincipal SecurityUserDetails userDetails);


    @Operation(summary = "기부해준 사람들의 목록 불러오기", description = "기부해준 사람들의 목록을 조회하기 위한 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "기부해준 사람 조회하기 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                        [
                                            {
                                                "nickname": "기부자1",
                                                "donateCount": 5
                                            },
                                            {
                                                "nickname": "기부자2",
                                                "donateCount": 10
                                            }
                                        ]
                                    """)
                    })
            )
    })
    ResponseEntity<?> getDonorsList(
            @AuthenticationPrincipal SecurityUserDetails userDetails);
}
