package gible.domain.event.dto;

import java.util.UUID;

public record EventInfoRes(
        UUID eventId,
        String title,
        String imageUrl
) {
}
