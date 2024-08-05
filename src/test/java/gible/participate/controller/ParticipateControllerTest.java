package gible.participate.controller;

import gible.domain.event.entity.Event;
import gible.domain.participate.controller.ParticipateController;
import gible.domain.participate.dto.ParticipationEventRes;
import gible.domain.participate.entity.Participate;
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

@WebMvcTest(ParticipateController.class)
public class ParticipateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ParticipateService participateService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private User user;
    private SecurityUserDetails userDetails;
    private UUID eventId;

    private Event event1;
    private Event event2;
    private Participate participate1;
    private Participate participate2;


    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .defaultRequest(post("/**").with(csrf()))
                .defaultRequest(get("/**").with(csrf()))
                .build();

        this.user = User.builder()
                .email("test@gmail.com")
                .role(Role.USER)
                .build();

        this.userDetails = SecurityUserDetails.builder().user(user).build();
        this.eventId = UUID.randomUUID();

        createParticipate();
    }

    private void createParticipate() {
        this.event1 = Event.builder()
                .title("이벤트1")
                .content("내용1")
                .build();

        this.event2 = Event.builder()
                .title("이벤트2")
                .content("내용2")
                .build();

        this.participate1 = Participate.builder()
                .user(user)
                .event(event1)
                .build();

        this.participate2 = Participate.builder()
                .user(user)
                .event(event2)
                .build();
    }

    @Test
    @DisplayName("이벤트 참여 테스트")
    void participationEventTest() throws Exception {
        // given
        doNothing().when(participateService).participationEvent(userDetails.getId(), eventId);

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

    @Test
    @DisplayName("사용자가 참여한 이벤트 목록 조회하기 테스트")
    void getAllParticipationEventsTest() throws Exception {
        // given
        List<ParticipationEventRes> participates = List.of(
                ParticipationEventRes.fromEntity(participate1),
                ParticipationEventRes.fromEntity(participate2)
        );

        given(participateService.getAllParticipationEvents(userDetails.getId())).willReturn(participates);

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/my-page/participation-event")
                        .with(user(userDetails))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].event.eventId").value(event1.getId()))
                .andExpect(jsonPath("$[0].event.title").value(event1.getTitle()))
                .andExpect(jsonPath("$[1].event.eventId").value(event2.getId()))
                .andExpect(jsonPath("$[1].event.title").value(event2.getTitle()));
    }
}