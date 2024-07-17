package gible.participate.controller;

import gible.domain.participate.controller.ParticipateController;
import gible.domain.participate.service.ParticipateService;
import gible.domain.security.common.SecurityUserDetails;
import gible.domain.security.jwt.JwtAuthenticationFilter;
import gible.domain.user.entity.Role;
import gible.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ParticipateController.class)
public class ParticipateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ParticipateService participateService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private User user;
    private UserDetails userDetails;
    private String userEmail;
    private UUID eventId;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .defaultRequest(post("/**").with(csrf()))
                .build();

        this.user = User.builder()
                .email("test@gmail.com")
                .role(Role.USER)
                .build();

        this.userDetails = SecurityUserDetails.builder().user(user).build();
        userEmail = "test@gmail.com";
        this.eventId = UUID.randomUUID();
    }

    @Test
    @DisplayName("이벤트 참여 테스트")
    void participationEventTest() throws Exception {
        // given
        doNothing().when(participateService).participationEvent(userEmail, eventId);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/event/{eventId}/participation", eventId)
                        .with(user(userDetails))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("참여 완료."));
    }

    @Test
    @DisplayName("이벤트 참여 실패 테스트 - 인증되지 않은 사용자")
    void participationEventFailedNotAuthorizationTest() throws Exception {
        // given

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/event/{eventId}/participation", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}