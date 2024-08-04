package gible.domain.review.dto;

import gible.domain.review.entity.Review;

import java.time.LocalDateTime;
import java.util.UUID;


public record ReviewDetailRes(
        UUID reviewId,
        String title,
        String content,
        String nickname,
        String imageUrl,
        UUID writerId,
        String email,
        UUID postId,
        boolean isPermitted,
        LocalDateTime createdAt
) {
    public static ReviewDetailRes fromEntity(Review review, boolean isPermitted) {
        return new ReviewDetailRes(
                review.getId(),review.getTitle(), review.getContent(), review.getWriter().getNickname(),
                review.getImageUrl(), review.getWriter().getId(), review.getWriter().getEmail(),
                review.getPostId(), isPermitted, review.getCreatedAt());
    }
}
