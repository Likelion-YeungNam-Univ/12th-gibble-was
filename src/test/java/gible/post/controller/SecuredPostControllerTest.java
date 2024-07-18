package gible.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gible.domain.post.controller.PostController;
import gible.domain.post.dto.PostReq;
import gible.domain.post.service.PostService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
public class SecuredPostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private SecurityUserDetails userDetails;

    private User user;

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
    @DisplayName("게시글 업로드 성공 테스트")
    void savePostTest() throws Exception {
        // given
        PostReq postReq = new PostReq("제목", "내용", "주소", "이름", 20);

        doNothing().when(postService).savePost(any(PostReq.class), eq(userDetails.getId()));

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/post/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(userDetails))
                        .content(objectMapper.writeValueAsString(postReq))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.response").value("게시글 업로드 완료."));
    }
}
