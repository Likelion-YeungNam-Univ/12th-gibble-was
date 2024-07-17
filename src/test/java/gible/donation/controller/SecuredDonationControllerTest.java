package gible.donation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gible.domain.donation.controller.DonationController;
import gible.domain.donation.dto.DonationPostInfoRes;
import gible.domain.donation.dto.DonationReq;
import gible.domain.donation.dto.DonationSenderInfoRes;
import gible.domain.donation.entity.Donation;
import gible.domain.donation.service.DonationService;
import gible.domain.post.entity.Post;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DonationController.class)
public class SecuredDonationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DonationService donationService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private User user1;

    private UserDetails userDetails;

    private User user;

    private UUID postId;

    private Post post;
    private Donation donation1;
    private Donation donation2;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .defaultRequest(post("/**").with(csrf()))
                .defaultRequest(get("/**").with(csrf()))
                .build();

        this.postId = UUID.randomUUID();

        this.user = User.builder()
                .email("test@gmail.com")
                .role(Role.USER)
                .build();

        this.userDetails = SecurityUserDetails.builder().user(user).build();

        this.post = Post.builder()
                .title("제목")
                .build();

        createDonation();
    }

    private void createDonation() {
        this.donation1 = Donation.builder()
                .donateCount(2)
                .post(post)
                .sender(user)
                .receiver(user1)
                .build();

        this.donation2 = Donation.builder()
                .donateCount(1)
                .post(post)
                .sender(user1)
                .receiver(user)
                .build();
    }

    @Test
    @DisplayName("기부하기 테스트")
    void donateTest() throws Exception {
        // given
        DonationReq donationReq = new DonationReq(2);

        doNothing().when(donationService).donate(donationReq, userDetails.getUsername(), postId);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/post/{postId}/donate", postId)
                        .with(user(userDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(donationReq))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("기부 성공."));
    }

    @Test
    @DisplayName("기부하기 실패 테스트 - 인증되지 않은 사용자")
    void donateFailedNotAuthorizationTest() throws Exception {
        // given

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/post/{postId}/donate", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(donationService))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("기부한 게시글에 대한 정보 불러오기 테스트")
    void getPostDonationDetailsTest() throws Exception {
        // given
        List<DonationPostInfoRes> donations = List.of(
                DonationPostInfoRes.fromEntity(donation1)
        );

        given(donationService.getPostDonationDetails(userDetails.getUsername())).willReturn(donations);

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/donation/my-donation")
                        .with(user(userDetails))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value(donations.get(0).title()))
                .andExpect(jsonPath("$[0].nickname").value(donations.get(0).nickname()));
    }

    @Test
    @DisplayName("기부해준 사람들의 목록 불러오기 테스트")
    void getDonorsListTest() throws Exception {
        // given
        List<DonationSenderInfoRes> donations = List.of(
                DonationSenderInfoRes.fromEntity(donation2)
        );

        given(donationService.getDonorsList(userDetails.getUsername())).willReturn(donations);

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/donation/received-donation")
                        .with(user(userDetails))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nickname").value(donations.get(0).nickname()))
                .andExpect(jsonPath("$[0].donateCount").value(donations.get(0).donateCount()));
    }
}
