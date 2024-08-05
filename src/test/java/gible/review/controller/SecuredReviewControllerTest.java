package gible.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gible.domain.review.controller.ReviewController;
import gible.domain.review.dto.ReviewReq;
import gible.domain.review.service.ReviewService;
import gible.domain.security.common.SecurityUserDetails;
import gible.domain.security.jwt.JwtAuthenticationFilter;
import gible.domain.user.entity.Role;
import gible.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
public class SecuredReviewControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private User user;
    private SecurityUserDetails userDetails;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .defaultRequest(post("/**").with(csrf()))
                .build();


        this. user = User.builder()
                .email("test@gmail.com")
                .role(Role.USER)
                .build();

        this.userDetails = SecurityUserDetails.builder().user(user).build();
    }

    @Test
    @DisplayName("리뷰 등록 테스트")
    void uploadReview() throws Exception {
        // given
        ReviewReq reviewReq = new ReviewReq("제목", "내용", "http://1234.555");

        doNothing().when(reviewService).uploadReview(userDetails.getId(), reviewReq);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/review/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(userDetails))
                        .content(objectMapper.writeValueAsBytes(reviewReq))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.response").value("리뷰 업로드 성공"));
    }

    @Test
    @DisplayName("잘못된 리뷰 등록")
    void uploadWrongReview() throws Exception {
        // given
        ReviewReq reviewReq = new ReviewReq("", "", "");

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/review/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(userDetails))
                        .content(objectMapper.writeValueAsBytes(reviewReq))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("제목은 필수 작성 항목입니다."))
                .andExpect(jsonPath("$.content").value("내용은 필수 작성 항목입니다."));
    }
}
