package gible.domain.review.dto;

import gible.domain.review.entity.Review;

import java.util.UUID;

public record ReviewSummaryRes(
        UUID id,
        String title,
        String content,
        String nickname
) {
    public static ReviewSummaryRes fromEntity(Review review) {
        return new ReviewSummaryRes(review.getId(), review.getTitle(), review.getContent(), review.getWriter().getNickname());
    }
}
