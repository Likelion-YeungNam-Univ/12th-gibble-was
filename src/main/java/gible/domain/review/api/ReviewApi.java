package gible.domain.review.api;

import gible.domain.review.dto.ReviewReq;
import gible.domain.security.common.SecurityUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Tag(name = "[리뷰 API]", description = "리뷰 관련 API")
public interface ReviewApi {

    @Operation(summary = "리뷰 목록 조회", description = "리뷰 목록 조회하기 위한 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리뷰 목록 가져오기 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                        {
                                            "totalPages": 1,
                                            "totalElements": 2,
                                            "first": true,
                                            "last": true,
                                            "size": 10,
                                            "content": [
                                                {
                                                    "id": "a12b34cd-56ef-78gh-90ij-1234567890kl",
                                                    "title": "리뷰 제목 1",
                                                    "content": "리뷰 내용 1",
                                                    "nickname": "리뷰자1"
                                                },
                                                {
                                                    "id": "a12b34cd-56ef-78gh-90ij-1234567890kl",
                                                    "title": "리뷰 제목 2",
                                                    "content": "리뷰 내용 2",
                                                    "nickname": "리뷰자2"
                                                }
                                            ],
                                            "number": 0,
                                            "sort": {
                                                "empty": false,
                                                "sorted": true,
                                                "unsorted": false
                                            },
                                            "numberOfElements": 2,
                                            "pageable": {
                                                "pageNumber": 0,
                                                "pageSize": 10,
                                                "sort": {
                                                    "empty": false,
                                                    "sorted": true,
                                                    "unsorted": false
                                                },
                                                "offset": 0,
                                                "paged": true,
                                                "unpaged": false
                                            },
                                            "empty": false
                                        }
                                    """)
                    })
            )
    })
    ResponseEntity<?> getReviews(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    );

    @Operation(summary = "특정 리뷰 조회", description = "특정 리뷰에 대한 조회하기 위한 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "특정 리뷰 조회하기 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                        {
                                            "title": "리뷰 제목 1",
                                            "content": "리뷰 내용 1",
                                            "nickname": "리뷰자1",
                                            "imageUrl": "http://example.com/image.jpg",
                                            "writerId": "f47ac10b-58cc-4372-a567-0e02b2c3d479"
                                        }
                                    """)
                    })
            )
    })
    ResponseEntity<?> getReview(@AuthenticationPrincipal SecurityUserDetails userDetails,
                                @PathVariable UUID reviewId);

    @Operation(summary = "리뷰 업로드", description = "리뷰 업로드하기 위한 API")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "리뷰 업로드 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                        {
                                            "response": "리뷰 업로드 성공"
                                        }
                                    """)
                    })),
    })
    ResponseEntity<?> uploadReview(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @Valid @RequestBody ReviewReq reviewReq
    );

    @Operation(summary = "리뷰 삭제", description = "리뷰 삭제하기 위한 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리뷰 삭제 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                        {
                                            "response": "리뷰 삭제 성공"
                                        }
                                    """)
                    })),
    })
    ResponseEntity<?> deleteReview(@PathVariable UUID reviewId);
}
