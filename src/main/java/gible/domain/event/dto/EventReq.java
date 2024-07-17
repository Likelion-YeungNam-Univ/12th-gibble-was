package gible.domain.event.dto;

import gible.domain.event.entity.Event;
import jakarta.validation.constraints.NotBlank;

public record EventReq(
        @NotBlank(message = "제목은 필수 작성 항목입니다.")
        String title,
        @NotBlank(message = "내용은 필수 작성 항목입니다.")
        String content,
        String imageUrl
) {
    public static Event toEntity(EventReq eventReq) {
        return Event.builder()
                .title(eventReq.title())
                .content(eventReq.content())
                .imageUrl(eventReq.imageUrl())
                .build();
    }
}
