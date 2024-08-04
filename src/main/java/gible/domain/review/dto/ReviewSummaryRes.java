package gible.domain.review.dto;

import gible.domain.review.entity.Review;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReviewSummaryRes(
        UUID id,
        String title,
        String content,
        String imageUrl,
        String nickname,
        LocalDateTime createdAt
) {
    public static ReviewSummaryRes fromEntity(Review review) {
        return new ReviewSummaryRes(review.getId(), review.getTitle(),
                review.getContent(), review.getImageUrl(), review.getWriter().getNickname(), review.getCreatedAt());
    }
}
