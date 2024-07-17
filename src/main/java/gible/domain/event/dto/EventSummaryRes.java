package gible.domain.event.dto;

import gible.domain.event.entity.Event;

import java.util.UUID;

public record EventSummaryRes(
        UUID eventId,
        String title,
        String imageUrl
) {
    public static EventSummaryRes fromEntity(Event event) {
        return new EventSummaryRes(event.getId(), event.getTitle(), event.getImageUrl());
    }
}
