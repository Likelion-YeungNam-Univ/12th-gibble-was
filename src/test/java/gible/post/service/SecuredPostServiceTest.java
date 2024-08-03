package gible.post.service;

import gible.domain.post.dto.PostReq;
import gible.domain.post.entity.Post;
import gible.domain.post.repository.PostRepository;
import gible.domain.post.service.PostService;
import gible.domain.security.common.SecurityUserDetails;
import gible.domain.user.entity.Role;
import gible.domain.user.entity.User;
import gible.domain.user.repository.UserRepository;
import gible.exception.CustomException;
import gible.exception.error.ErrorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class SecuredPostServiceTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    private User user;

    private UUID userId;
    private UUID postId;

    private Post post;
    private PostReq updatePostReq;

    @BeforeEach
    void setUp() {
        this.updatePostReq =
                new PostReq("제목수정", "내용수정", "주소수정", 30);
        this.userId = UUID.randomUUID();
        this.postId = UUID.randomUUID();

        this.user = User.builder()
                .email("test@gmail.com")
                .role(Role.USER)
                .build();
        setUserId(user, userId);

        createPost();

        SecurityUserDetails securityUserDetails = SecurityUserDetails.builder().user(user).build();
        SecurityContextHolder.setContext(new SecurityContextImpl(new TestingAuthenticationToken(securityUserDetails, null)));
    }

    /* User의 id를 임의로 설정 */
    private void setUserId(User user, UUID id) {
        try {
            Field idField = User.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(user, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void createPost() {
        this.post = Post.builder()
                .title("제목")
                .content("내용")
                .address("주소")
                .wantedCard(20)
                .writer(user)
                .build();
    }

    @Test
    @DisplayName("게시글 업데이트 테스트")
    void updatePostTest() {
        // given
        given(postRepository.findById(postId)).willReturn(Optional.of(post));
        given(postRepository.findWriterIdByPostId(postId)).willReturn(userId);

        // when
        postService.updatePost(updatePostReq, postId);

        // then
        assertEquals("제목수정", post.getTitle());
        assertEquals("내용수정", post.getContent());
        assertEquals("주소수정", post.getAddress());
        assertEquals(30, post.getWantedCard());
    }

    @Test
    @DisplayName("게시글 업데이트 실패 테스트 - 권한 없음")
    void updatePostFailedByUnauthorizedTest() {
        // given
        UUID otherUserId = UUID.randomUUID();

        given(postRepository.findById(postId)).willReturn(Optional.of(post));
        given(postRepository.findWriterIdByPostId(postId)).willReturn(otherUserId);

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            postService.updatePost(updatePostReq, postId);
        });

        // then
        assertEquals(ErrorType.UNAUTHORIZED, exception.getErrortype());
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    void deletePostTest() {
        // given
        given(postRepository.findWriterIdByPostId(postId)).willReturn(userId);

        // when
        postService.deletePost(postId);

        // then
        verify(postRepository, times(1)).deleteById(postId);
    }

    @Test
    @DisplayName("게시글 삭제 실패 테스트 - 권한 없음")
    void deletePostFailedByUnauthorizedTest() {
        // given
        UUID otherUserId = UUID.randomUUID();
        given(postRepository.findWriterIdByPostId(postId)).willReturn(otherUserId);

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            postService.deletePost(postId);
        });

        // then
        assertEquals(ErrorType.UNAUTHORIZED, exception.getErrortype());
    }
}
