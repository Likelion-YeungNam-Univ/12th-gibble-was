package gible.event.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gible.domain.event.controller.EventController;
import gible.domain.event.dto.EventDetailRes;
import gible.domain.event.dto.EventReq;
import gible.domain.event.dto.EventSummaryRes;
import gible.domain.event.entity.Event;
import gible.domain.event.service.EventService;
import gible.domain.security.jwt.JwtAuthenticationFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

@WebMvcTest(EventController.class)
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EventService eventService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private Event event1;
    private Event event2;
    private UUID eventId;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();

        this.eventId = UUID.randomUUID();
        createEvent();
    }

    private void createEvent() {
        this.event1 = Event.builder()
                .title("제목1")
                .content("내용1")
                .imageUrl("http://qewqeqw.asd")
                .build();

        this.event2 = Event.builder()
                .title("제목2")
                .content("내용2")
                .imageUrl("http://asdasd.zxc")
                .build();
    }

    @Test
    @DisplayName("이벤트 등록 테스트")
    void saveEventTest() throws Exception {
        // given
        EventReq eventReq = new EventReq("이벤트 제목", "이벤트 내용", "http://qweqwe.asd");

        doNothing().when(eventService).saveEvent(eventReq);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/event/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(eventReq))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.response").value("이벤트 작성 성공."));
    }

    @Test
    @DisplayName("이벤트 등록 실패 테스트 - 조건 불충족")
    void saveEventInvalidTest() throws Exception {
        // given
        EventReq eventReq = new EventReq("", "", "");

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/event/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(eventReq))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("제목은 필수 작성 항목입니다."))
                .andExpect(jsonPath("$.content").value("내용은 필수 작성 항목입니다."));
    }

    @Test
    @DisplayName("이벤트 목록 조회 테스트")
    void getAllEventsTest() throws Exception {
        // given
        Page<EventSummaryRes> events = new PageImpl<>(List.of(
                EventSummaryRes.fromEntity(event1),
                EventSummaryRes.fromEntity(event2)
        ));

        given(eventService.getAllEvents(any(Pageable.class))).willReturn(events);

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/event")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].title").value(event1.getTitle()))
                .andExpect(jsonPath("$.content[0].imageUrl").value(event1.getImageUrl()))
                .andExpect(jsonPath("$.content[1].title").value(event2.getTitle()))
                .andExpect(jsonPath("$.content[1].imageUrl").value(event2.getImageUrl()));
    }

    @Test
    @DisplayName("특정 이벤트 조회 테스트")
    void getEventTest() throws Exception {
        // given
        EventDetailRes event = EventDetailRes.fromEntity(event1);

        given(eventService.getEvent(eventId)).willReturn(event);

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/event/{eventId}", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId").value(event.eventId()))
                .andExpect(jsonPath("$.title").value(event.title()))
                .andExpect(jsonPath("$.content").value(event.content()))
                .andExpect(jsonPath("$.imageUrl").value(event.imageUrl()));
    }

    @Test
    @DisplayName("이벤트 수정 테스트")
    void updateEventTest() throws Exception {
        // given
        EventReq updateEventReq = new EventReq("제목 수정", "내용 수정", "http://asdasd.zxc");

        doNothing().when(eventService).updateEvent(updateEventReq, eventId);

        // when
        ResultActions resultActions = mockMvc.perform(
                put("/event/upload/{eventId}", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(updateEventReq))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("이벤트 수정 성공."));
    }

    @Test
    @DisplayName("이벤트 수정 실패 테스트 - 조건 불충족")
    void updateEventInvalidTest() throws Exception {
        // given
        EventReq updateEventReq = new EventReq(" ", " ", " ");

        // when
        ResultActions resultActions = mockMvc.perform(
                put("/event/upload/{eventId}", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(updateEventReq))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("제목은 필수 작성 항목입니다."))
                .andExpect(jsonPath("$.content").value("내용은 필수 작성 항목입니다."));
    }

    @Test
    @DisplayName("이벤트 삭제 테스트")
    void deleteEventTest() throws Exception {
        // given
        doNothing().when(eventService).deleteEvent(eventId);

        // when
        ResultActions resultActions = mockMvc.perform(
                delete("/event/{eventId}", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("이벤트 삭제 성공."));
    }
}
