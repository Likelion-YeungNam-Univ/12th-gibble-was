package gible.domain.event.dto;

import gible.domain.event.entity.Event;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventDetailRes(
        UUID eventId,
        String title,
        String content,
        String duration,
        String imageUrl,
        LocalDateTime createdAt
) {
    public static EventDetailRes fromEntity(Event event) {
        return new EventDetailRes(event.getId(), event.getTitle(), event.getContent(), event.getDuration(), event.getImageUrl(), event.getCreatedAt());
    }
}
