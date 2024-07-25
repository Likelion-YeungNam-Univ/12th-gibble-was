package gible.domain.review.dto;

import gible.domain.review.entity.Review;

import java.util.UUID;

public record ReviewRes(
        UUID id,
        String title,
        String content,
        String nickname
) {
    public static ReviewRes fromEntity(Review review) {
        return new ReviewRes(review.getId(), review.getTitle(), review.getContent(), review.getWriter().getNickname());
    }
}
