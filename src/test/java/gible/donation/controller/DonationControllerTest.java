package gible.donation.controller;

import gible.domain.donation.controller.DonationController;
import gible.domain.donation.dto.DonationSenderInfoRes;
import gible.domain.donation.entity.Donation;
import gible.domain.donation.service.DonationService;
import gible.domain.post.entity.Post;
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

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DonationController.class)
public class DonationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DonationService donationService;

    @Mock
    private User user1;

    @Mock
    private User user2;

    @Mock
    private Post post;

    private Donation donation1;
    private Donation donation2;
    private UUID postId;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();

        this.postId = UUID.randomUUID();
        createDonation();
    }

    private void createDonation() {
        this.donation1 = Donation.builder()
                .donateCount(2)
                .post(post)
                .sender(user1)
                .receiver(user2)
                .build();

        this.donation2 = Donation.builder()
                .donateCount(1)
                .post(post)
                .sender(user2)
                .receiver(user1)
                .build();
    }

    @Test
    @DisplayName("게시글에 대한 기부자 목록 불러오기 테스트")
    void getDonorsForPost() throws Exception {
        // given
        List<DonationSenderInfoRes> donations = List.of(
                DonationSenderInfoRes.fromEntity(donation1),
                DonationSenderInfoRes.fromEntity(donation2)
        );

        given(donationService.getDonorsForPost(postId)).willReturn(donations);

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/post/{postId}/donators", postId)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nickname").value(donations.get(0).nickname()))
                .andExpect(jsonPath("$[0].donateCount").value(donations.get(0).donateCount()))
                .andExpect(jsonPath("$[1].nickname").value(donations.get(1).nickname()))
                .andExpect(jsonPath("$[1].donateCount").value(donations.get(1).donateCount()));
    }
}
