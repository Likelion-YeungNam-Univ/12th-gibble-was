package gible.review.controller;

import gible.domain.review.controller.ReviewController;
import gible.domain.review.dto.ReviewDetailRes;
import gible.domain.review.dto.ReviewSummaryRes;
import gible.domain.review.entity.Review;
import gible.domain.review.service.ReviewService;
import gible.domain.security.jwt.JwtAuthenticationFilter;
import gible.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ReviewController.class)
public class ReviewControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private User user;
    private Review review1;
    private Review review2;
    private Review review3;

    private UUID reviewId;
    private UUID userId;
    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();

        this.reviewId = UUID.randomUUID();
        this.userId = UUID.randomUUID();
        createReview();
    }
    private void createReview() {
        this.review1 = Review.builder()
                .title("제목1")
                .content("내용1")
                .imageUrl(null)
                .writer(user)
                .build();
        this.review2 = Review.builder()
                .title("제목2")
                .content("내용2")
                .imageUrl(null)
                .writer(user)
                .build();
        this.review3 = Review.builder()
                .title("제목3")
                .content("내용3")
                .imageUrl(null)
                .writer(user)
                .build();
    }


    @Test
    @DisplayName("전체 리뷰 불러오기 테스트")
    void getReviews() throws Exception {
        Page<ReviewSummaryRes> reviews = new PageImpl<>(List.of(
                ReviewSummaryRes.fromEntity(review1),
                ReviewSummaryRes.fromEntity(review2),
                ReviewSummaryRes.fromEntity(review3)
        ));

        given(reviewService.getReviews(any(Pageable.class))).willReturn(reviews);

        ResultActions resultActions = mockMvc.perform(
                get("/review")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.content[0].title").value(review1.getTitle()))
                .andExpect(jsonPath("$.content[0].content").value(review1.getContent()))
                .andExpect(jsonPath("$.content[1].title").value(review2.getTitle()))
                .andExpect(jsonPath("$.content[1].content").value(review2.getContent()))
                .andExpect(jsonPath("$.content[2].title").value(review3.getTitle()))
                .andExpect(jsonPath("$.content[2].content").value(review3.getContent()));

    }

    @Test
    @DisplayName("리뷰 불러오기 테스트")
    void getReviewById() throws Exception {
        ReviewDetailRes review = ReviewDetailRes.fromEntity(review1, true);

        given(reviewService.getReview(reviewId, userId)).willReturn(review);

        ResultActions resultActions = mockMvc.perform(
                get("/review/" + reviewId)
                .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(review.title()))
                .andExpect(jsonPath("$.content").value(review.content()))
                .andExpect(jsonPath("$.imageUrl").value(review.imageUrl()))
                .andExpect(jsonPath("$.nickname").value(review.nickname()));
    }

    @Test
    @DisplayName("리뷰 삭제 테스트")
    void deleteEventTest() throws Exception {
        // given
        doNothing().when(reviewService).deleteReview(reviewId);

        // when
        ResultActions resultActions = mockMvc.perform(
                delete("/review/" + reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("리뷰 삭제 성공"));
    }
}
