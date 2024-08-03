package gible.domain.post.api;

import gible.domain.post.dto.PostReq;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Tag(name = "[게시글 API]", description = "게시글 관련 API")
public interface PostApi {

    @Operation(summary = "게시글 생성", description = "게시글을 생성하기 위한 API")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "게시글 생성 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                        {
                                            "response": "게시글 업로드 완료"
                                        }
                                    """)
                    })
            )
    })
    ResponseEntity<?> savePost(@Valid @RequestBody PostReq postReq,
                               @AuthenticationPrincipal SecurityUserDetails userDetails);

    @Operation(summary = "사용자 정보 불러오기", description = "사용자의 정보를 불러오기 위한 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 정보 불러오기 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                        {
                                            "name": "홍길동",
                                            "phoneNumber": "010-1234-5678"
                                        }
                                    """)
                    })
            )
    })
    ResponseEntity<?> getUserInfo(@AuthenticationPrincipal SecurityUserDetails userDetails);

    @Operation(summary = "게시글 리스트 가져오기", description = "게시글 리스트를 조회하기 위한 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 리스트 가져오기 성공",
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
                                                    "eventId": "84a4e24c-d722-4479-ba8d-a3547f103fad",
                                                    "title": "이벤트1",
                                                    "imageUrl": null
                                                },
                                                {
                                                    "eventId": "e91c03e2-2821-4e60-9c7e-b054cccfc3a3",
                                                    "title": "이벤트2",
                                                    "imageUrl": null
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
    ResponseEntity<?> getAllPosts(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC, size = 10) Pageable pageable,
            @RequestParam(name = "search", required = false) String search);

    @Operation(summary = "특정 게시글 가져오기", description = "특정 게시글을 조회하기 위한 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 가져오기 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                        {
                                            "response": "게시글 업로드 완료"
                                        }
                                    """)
                    })
            )
    })
    ResponseEntity<?> getPost(@AuthenticationPrincipal SecurityUserDetails userDetails, @PathVariable UUID postId);

    @Operation(summary = "게시글 수정", description = "게시글을 수정하기 위한 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 수정하기 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                        {
                                            "response": "게시글 수정 완료"
                                        }
                                    """)
                    })
            )
    })
    ResponseEntity<?> updatePost(@Valid @RequestBody PostReq postReq,
                                 @PathVariable UUID postId);

    @Operation(summary = "게시글 삭제", description = "게시글을 삭제하기 위한 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 삭제하기 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                        {
                                            "response": "게시글 삭제 완료"
                                        }
                                    """)
                    })
            )
    })
    ResponseEntity<?> deletePost(@PathVariable UUID postId);
}
