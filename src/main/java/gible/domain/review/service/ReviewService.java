package gible.domain.review.service;

import gible.domain.review.dto.ReviewDetailRes;
import gible.domain.review.dto.ReviewReq;
import gible.domain.review.dto.ReviewSummaryRes;
import gible.domain.review.entity.Review;
import gible.domain.review.repository.ReviewRepository;
import gible.domain.user.entity.User;
import gible.domain.user.service.UserService;
import gible.exception.CustomException;
import gible.exception.error.ErrorType;
import gible.global.util.FirebaseDao;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final FirebaseDao reviewDao;

    @Transactional(readOnly = true)
    public Page<ReviewSummaryRes> getReviews(Pageable pageable) {
        return reviewRepository.findAll(pageable).map(ReviewSummaryRes::fromEntity);
    }

    @Transactional(readOnly = true)
    public ReviewDetailRes getReview(UUID reviewId) {
        return reviewRepository.findById(reviewId).map(ReviewDetailRes::fromEntity)
                .orElseThrow(()-> new CustomException(ErrorType.EVENT_NOT_FOUND));
    }

    @Transactional
    public void uploadReview(UUID userId, ReviewReq reviewReq) {
        User user = userService.findById(userId);
        reviewRepository.save(ReviewReq.toEntity(reviewReq, user));
    }

    @Transactional
    public void deleteReview(UUID reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(()-> new CustomException(ErrorType.EVENT_NOT_FOUND));
        reviewDao.delete(review.getReviewImageId());
        reviewRepository.delete(review);
    }
}
