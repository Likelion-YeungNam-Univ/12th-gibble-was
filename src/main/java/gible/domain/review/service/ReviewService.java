package gible.domain.review.service;

import gible.domain.review.dto.ReviewReq;
import gible.domain.review.dto.ReviewRes;
import gible.domain.review.entity.Review;
import gible.domain.review.repository.ReviewRepository;
import gible.domain.user.entity.User;
import gible.domain.user.service.UserService;
import gible.exception.CustomException;
import gible.exception.error.ErrorType;
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

    @Transactional(readOnly = true)
    public Page<ReviewRes> getReviews(Pageable pageable) {
        return reviewRepository.findAll(pageable).map(ReviewRes::fromEntity);
    }

    @Transactional(readOnly = true)
    public ReviewRes getReview(UUID reviewId) {
        return reviewRepository.findById(reviewId).map(ReviewRes::fromEntity)
                .orElseThrow(()-> new CustomException(ErrorType.EVENT_NOT_FOUND));
    }

    @Transactional
    public void uploadReview(UUID userId, ReviewReq reviewReq) {
        User user = userService.findById(userId);
        reviewRepository.save(ReviewReq.toEntity(reviewReq, user));
    }

    @Transactional
    public void deleteReview(UUID reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}
