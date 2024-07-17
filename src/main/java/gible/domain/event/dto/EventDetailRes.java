package gible.domain.event.dto;

import gible.domain.event.entity.Event;

import java.util.UUID;

public record EventDetailRes(
        UUID eventId,
        String title,
        String content,
        String imageUrl
) {
    public static EventDetailRes fromEntity(Event event) {
        return new EventDetailRes(event.getId(), event.getTitle(), event.getContent(), event.getImageUrl());
    }
}
