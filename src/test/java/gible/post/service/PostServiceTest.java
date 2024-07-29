package gible.post.service;

import gible.domain.mail.service.MailService;
import gible.domain.post.dto.PostDetailRes;
import gible.domain.post.dto.PostReq;
import gible.domain.post.dto.PostSummaryRes;
import gible.domain.post.entity.Post;
import gible.domain.post.repository.PostRepository;
import gible.domain.post.service.PostService;
import gible.domain.user.entity.User;
import gible.domain.user.repository.UserRepository;
import gible.exception.CustomException;
import gible.exception.error.ErrorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private MailService mailService;

    @InjectMocks
    private PostService postService;

    @Mock
    private User user;

    private PostReq postReq;
    private UUID userId;
    private UUID postId;

    private Post post1;
    private Post post2;
    private Post post3;

    @BeforeEach
    void setUp() {
        this.postReq = new PostReq("제목", "내용", "주소", 20);
        this.userId = UUID.randomUUID();
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

    @Test
    @DisplayName("게시글 작성 성공 테스트")
    void savePostTest() {
        // given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Post post = postReq.toEntity(postReq);
        when(postRepository.save(any(Post.class))).thenReturn(post);

        // when
        postService.savePost(postReq, userId);

        // then
        verify(userRepository, times(1)).findById(userId);
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    @DisplayName("게시글 작성 실패 테스트 - 사용자 없음")
    void savePostFailedByUserNotFoundTest() {
        // given
        when(userRepository.findById(userId)).thenThrow(new CustomException(ErrorType.USER_NOT_FOUND));

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            postService.savePost(postReq, userId);
        });

        // then
        assertEquals(ErrorType.USER_NOT_FOUND, exception.getErrortype());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("전체 게시글 불러오기 테스트")
    void getAllPostsTest() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Post> posts = new PageImpl<>(List.of(
                post1, post2, post3
        ));

        given(postRepository.findAll(pageable)).willReturn(posts);

        // when
        Page<PostSummaryRes> postSummaryResPage = postService.getAllPosts(pageable);

        // then
        assertNotNull(postSummaryResPage);
        assertEquals(3, postSummaryResPage.getTotalElements());
        assertEquals("제목1", postSummaryResPage.getContent().get(0).title());
        assertEquals("제목2", postSummaryResPage.getContent().get(1).title());
        assertEquals("title", postSummaryResPage.getContent().get(2).title());
    }

    @Test
    @DisplayName("특정 게시글 불러오기 테스트")
    void getPostTest() {
        // given
        given(postRepository.findById(postId)).willReturn(Optional.ofNullable(post1));

        // when
        PostDetailRes post = postService.getPost(postId);

        // then
        assertNotNull(post);
        assertEquals("제목1", post.title());
        assertEquals("내용1", post.content());
        assertEquals("주소1", post.address());
    }

    @Test
    @DisplayName("특정 게시글 불러오기 실패 테스트 - 존재하지 않는 게시글")
    void getPostFailedByPostNotFoundTest() {
        // given
        when(postRepository.findById(postId)).thenThrow(new CustomException(ErrorType.POST_NOT_FOUND));

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            postService.getPost(postId);
        });

        // then
        assertEquals(ErrorType.POST_NOT_FOUND, exception.getErrortype());
        verify(postRepository, times(1)).findById(postId);
    }

    @Test
    @DisplayName("검색을 통한 게시글 불러오기 테스트")
    void getPostsByKeywordTest() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Post> posts = new PageImpl<>(List.of(
                post1, post2
        ));

        String search = "제목";

        given(postRepository.findByTitleContaining(search, pageable)).willReturn(posts);

        // when
        Page<PostSummaryRes> postSummaryResPage = postService.getPostsByKeyword(search, pageable);

        // then
        assertNotNull(postSummaryResPage);
        assertEquals("제목1", postSummaryResPage.getContent().get(0).title());
        assertEquals("제목2", postSummaryResPage.getContent().get(1).title());
    }
}
