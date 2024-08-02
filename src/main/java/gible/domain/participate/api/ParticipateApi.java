package gible.domain.participate.api;

import gible.domain.security.common.SecurityUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Tag(name = "[이벤트 참여 API]", description = "이벤트 참여 관련 API")
public interface ParticipateApi {

    @Operation(summary = "이벤트 참여", description = "이벤트 참여하기 위한 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이벤트 참여 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                        {
                                            "response": "참여 완료."
                                        }
                                    """)
                    })),
    })
    ResponseEntity<?> participationEvent(@AuthenticationPrincipal SecurityUserDetails userDetails,
                                         @PathVariable UUID eventId);

    @Operation(summary = "사용자가 참여한 이벤트 목록 조회하기", description = "사용자가 참여한 이벤트 목록 조회하기 위한 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "참여한 이벤트 리스트 조회하기 성공",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(value = """
                                        [
                                            {
                                                "event": {
                                                    "eventId": "d290f1ee-6c54-4b01-90e6-d701748f0851",
                                                    "title": "이벤트1"
                                                }
                                            },
                                            {
                                                "event": {
                                                    "eventId": "c56a4180-65aa-42ec-a945-5fd21dec0538",
                                                    "title": "이벤트2"
                                                }
                                            }
                                        ]
                                    """)
                    })
            )
    })
    ResponseEntity<?> getAllParticipationEvents(
            @AuthenticationPrincipal SecurityUserDetails userDetails);
}
