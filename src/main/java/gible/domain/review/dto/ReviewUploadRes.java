package gible.domain.review.dto;

import java.util.UUID;

public record ReviewUploadRes(UUID reviewId) {
    public static ReviewUploadRes from(UUID reviewId){
        return new ReviewUploadRes(reviewId);
    }
}