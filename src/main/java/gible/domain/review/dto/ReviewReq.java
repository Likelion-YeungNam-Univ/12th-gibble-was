package gible.domain.review.dto;

import gible.domain.review.entity.Review;
import gible.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;

public record ReviewReq(
        @NotBlank(message = "제목은 필수 작성 항목입니다.")
        String title,
        @NotBlank(message = "내용은 필수 작성 항목입니다.")
        String content,
        String imageUrl
) {
    public static Review toEntity(ReviewReq reviewReq, User user) {
        return Review.builder()
                .title(reviewReq.title())
                .content(reviewReq.content())
                .imageUrl(reviewReq.imageUrl())
                .writer(user)
                .build();
    }
}
