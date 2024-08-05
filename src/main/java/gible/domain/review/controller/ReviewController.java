package gible.domain.review.controller;

import gible.domain.review.api.ReviewApi;
import gible.domain.review.dto.ReviewReq;
import gible.domain.review.service.ReviewService;
import gible.domain.security.common.SecurityUserDetails;
import gible.global.common.response.SuccessRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController implements ReviewApi {
    private final ReviewService reviewService;

    /* 리뷰 목록 가져오기 */
    @Override
    @GetMapping("")
    public ResponseEntity<?> getReviews(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(name = "search", required = false) String search
    ) {
        if(search == null) {
            return ResponseEntity.ok(reviewService.getReviews(pageable));
        }
        return ResponseEntity.ok(reviewService.getReviewsByKeyword(pageable, search));
    }

    /* 리뷰 가져오기 */
    @Override
    @GetMapping("/{reviewId}")
    public ResponseEntity<?> getReview(@AuthenticationPrincipal SecurityUserDetails userDetails,
                                       @PathVariable UUID reviewId) {
        return ResponseEntity.ok(reviewService.getReview(userDetails.getId(), reviewId));
    }

    /* 리뷰 업로드 */
    @Override
    @PostMapping("/upload")
    public ResponseEntity<?> uploadReview(
            @AuthenticationPrincipal SecurityUserDetails userDetails,
            @Valid @RequestBody ReviewReq reviewReq
            ) {

        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.uploadReview(userDetails.getId(), reviewReq));
    }

    @Override
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable UUID reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok(SuccessRes.from("리뷰 삭제 성공"));
    }

}
