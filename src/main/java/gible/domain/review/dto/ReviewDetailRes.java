package gible.domain.review.dto;

import gible.domain.review.entity.Review;

import java.util.UUID;

public record ReviewDetailRes(
        String title,
        String content,
        String nickname,
        String imageUrl
) {
    public static ReviewDetailRes fromEntity(Review review) {
        return new ReviewDetailRes(review.getTitle(), review.getContent(), review.getWriter().getNickname(), review.getImageUrl());
    }
}
