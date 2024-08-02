package gible.domain.event.api;

import gible.domain.event.dto.EventReq;
import gible.domain.event.entity.Region;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Tag(name = "[이벤트 API]", description = "이벤트 관련 API")
public interface EventApi {

    @Operation(summary = "이벤트 등록", description = "이벤트 등록하기 위한 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이벤트 등록 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                        {
                                            "response": "이벤트 작성 성공."
                                        }
                                    """)
                    })),
    })
    ResponseEntity<?> saveEvent(@Valid @RequestBody EventReq eventReq);

    @Operation(summary = "이벤트 목록 조회", description = "이벤트 목록 조회하기 위한 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이벤트 목록 가져오기 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                        {
                                            "totalPages": 1,
                                            "totalElements": 2,
                                            "first": true,
                                            "last": true,
                                            "size": 12,
                                            "content": [
                                                {
                                                    "eventId": "84a4e24c-d722-4479-ba8d-a3547f103fad",
                                                    "title": "이벤트1",
                                                    "imageUrl": "http://image-url.co.kr"
                                                },
                                                {
                                                    "eventId": "e91c03e2-2821-4e60-9c7e-b054cccfc3a3",
                                                    "title": "이벤트2",
                                                    "imageUrl": "http://image-url.co.kr"
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
                                                "pageSize": 12,
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
    ResponseEntity<?> getAllEvents(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC, size = 12) Pageable pageable,
            @RequestParam(name = "region", required = false) Region region);

    @Operation(summary = "특정 이벤트 조회", description = "특정 이벤트에 대한 조회하기 위한 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "특정 이벤트 조회하기 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                        {
                                            "eventId": "e91c03e2-2821-4e60-9c7e-b054cccfc3a3",
                                            "title": "이벤트1",
                                            "content": "이벤트 참여해주세요~",
                                            "imageUrl": "http://image-url.co.kr"
                                        }
                                    """)
                    })
            )
    })
    ResponseEntity<?> getEvent(@PathVariable UUID eventId);

    @Operation(summary = "이벤트 수정", description = "이벤트 수정하기 위한 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이벤트 수정 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                        {
                                            "response": "이벤트 수정 성공."
                                        }
                                    """)
                    })),
    })
    ResponseEntity<?> updateEvent(@Valid @RequestBody EventReq updateEventReq,
                                  @PathVariable UUID eventId);

    @Operation(summary = "이벤트 삭제", description = "이벤트 삭제하기 위한 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이벤트 삭제 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                        {
                                            "response": "이벤트 삭제 성공."
                                        }
                                    """)
                    })),
    })
    ResponseEntity<?> deleteEvent(@PathVariable UUID eventId);
}
