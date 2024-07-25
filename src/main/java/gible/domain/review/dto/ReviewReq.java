package gible.domain.review.dto;

import jakarta.validation.constraints.NotBlank;

public record ReviewReq(
        @NotBlank String title,
        @NotBlank String content,
        String imageUrl
) {
    public static ReviewReq of(final String title, final String content, final String imageUrl) {
        return new ReviewReq(title, content, imageUrl);
    }
}
