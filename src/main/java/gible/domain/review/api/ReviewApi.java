package gible.domain.review.api;

import gible.domain.review.dto.ReviewReq;
import gible.domain.security.common.SecurityUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Tag(name = "[리뷰 API]", description = "리뷰 관련 API")
public interface ReviewApi {

    ResponseEntity<?> getReviews(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    );

    ResponseEntity<?> getReview(@PathVariable UUID reviewId);

    ResponseEntity<?> uploadReview(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @Valid @RequestBody ReviewReq reviewReq
    );

    ResponseEntity<?> deleteReview(@PathVariable UUID reviewId);
}
