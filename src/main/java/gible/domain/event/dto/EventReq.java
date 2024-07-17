package gible.domain.event.dto;

import gible.domain.event.entity.Event;

public record EventReq(
        String title,
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
