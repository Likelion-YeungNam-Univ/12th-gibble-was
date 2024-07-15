package gible.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gible.domain.post.controller.PostController;
import gible.domain.post.dto.PostDetailRes;
import gible.domain.post.dto.PostReq;
import gible.domain.post.dto.PostSummaryRes;
import gible.domain.post.entity.Post;
import gible.domain.post.service.PostService;
import gible.domain.user.entity.User;
import gible.exception.CustomException;
import gible.exception.error.ErrorType;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    @Mock
    private User user;

    private Post post1;
    private Post post2;
    private Post post3;
    private UUID postId;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();

        this.postId = UUID.randomUUID();
        createPost();
    }

    private void createPost() {
        this.post1 = Post.builder()
                .title("제목1")
                .content("내용1")
                .address("주소1")
                .name("작성자1")
                .wantedCard(20)
                .writer(user)
                .build();

        this.post2 = Post.builder()
                .title("제목2")
                .content("내용2")
                .address("주소2")
                .name("작성자2")
                .wantedCard(20)
                .writer(user)
                .build();

        this.post3 = Post.builder()
                .title("title")
                .content("내용3")
                .address("주소3")
                .name("작성자3")
                .wantedCard(20)
                .writer(user)
                .build();
    }


    /*@Test
    @DisplayName("게시글 업로드 성공 테스트")
    void savePostTest() throws Exception {
        // given
        PostReq postReq = new PostReq("제목", "내용", "주소", "이름", 20);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/post/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postReq))
        );

        // then
        resultActions
                .andExpect(status().isCreated())
                .andDo(print());
    }*/

    @Test
    @DisplayName("전체 게시글 불러오기 테스트")
    void getAllPosts() throws Exception {
        // given
        Page<PostSummaryRes> posts = new PageImpl<>(List.of(
                PostSummaryRes.fromEntity(post1),
                PostSummaryRes.fromEntity(post2),
                PostSummaryRes.fromEntity(post3)
        ));

        given(postService.getAllPosts(any(Pageable.class))).willReturn(posts);

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/post")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.content[0].title").value("제목1"))
                .andExpect(jsonPath("$.content[1].title").value("제목2"))
                .andExpect(jsonPath("$.content[2].title").value("title"));
    }

    @Test
    @DisplayName("검색한 단어에 대한 게시글 불러오기 테스트")
    void getPostsByKeywordTest() throws Exception {
        // given
        Page<PostSummaryRes> posts = new PageImpl<>(List.of(
                PostSummaryRes.fromEntity(post1),
                PostSummaryRes.fromEntity(post2)
        ));

        String search = "제목";
        given(postService.getPostsByKeyword(anyString(), any(Pageable.class))).willReturn(posts);

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/post")
                        .param("search", search)
                        .param("page", "0")
                        .param("size", "2")
                        .param("sort-by", "createdAt")
                        .param("direction", "DESC")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].title").value("제목1"))
                .andExpect(jsonPath("$.content[1].title").value("제목2"));
    }

    @Test
    @DisplayName("특정 게시글 조회 테스트")
    void getPostTest() throws Exception {
        // given
        PostDetailRes post = PostDetailRes.fromEntity(post1);
        given(postService.getPost(postId)).willReturn(post);

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/post/{postId}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("제목1"))
                .andExpect(jsonPath("$.content").value("내용1"))
                .andExpect(jsonPath("$.address").value("주소1"))
                .andExpect(jsonPath("$.name").value("작성자1"));
    }

    @Test
    @DisplayName("게시글 업데이트 성공 테스트")
    void updatePostTest() throws Exception {
        // given
        PostReq updatePostReq =
                new PostReq("제목수정", "내용수정", "주소수정", "이름수정", 30);

        doNothing().when(postService).updatePost(updatePostReq, postId);

        // when
        ResultActions resultActions = mockMvc.perform(
                put("/post/upload/{postId}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(updatePostReq))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("게시글 수정 완료."));
    }

    @Test
    @DisplayName("게시글 수정 실패 테스트 - 존재하지 않는 게시글")
    void updatePostFailedByPostNotFoundTest() throws Exception {
        // given
        PostReq updatePostReq =
                new PostReq("제목수정", "내용수정", "주소수정", "이름수정", 30);
        doThrow(new CustomException(ErrorType.POST_NOT_FOUND)).when(postService).updatePost(updatePostReq, postId);

        // when
        ResultActions resultActions = mockMvc.perform(
                put("/post/upload/{postId}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(updatePostReq))
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(ErrorType.POST_NOT_FOUND.getMessage()));
    }

    @Test
    @DisplayName("게시글 삭제 성공 테스트")
    void deletePostTest() throws Exception {
        // given
        doNothing().when(postService).deletePost(postId);

        // when
        ResultActions resultActions = mockMvc.perform(
                delete("/post/{postId}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("게시글 삭제 완료."));
    }

    @Test
    @DisplayName("게시글 삭제 실패 테스트 - 존재하지 않는 게시글")
    void deletePostFailedByPostNotFoundTest() throws Exception {
        // given
        doThrow(new CustomException(ErrorType.POST_NOT_FOUND)).when(postService).deletePost(postId);

        // when
        ResultActions resultActions = mockMvc.perform(
                delete("/post/{postId}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(ErrorType.POST_NOT_FOUND.getMessage()));
    }
}
