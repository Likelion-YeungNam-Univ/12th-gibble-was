package gible.mail.service;

import gible.domain.mail.service.MailService;
import gible.domain.post.entity.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class MailServiceTest {

    @Autowired
    private MailService mailService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String emailUsername;

    @Value("${web.post-url}")
    private String postURL;

    private Post post;
    private UUID postId;

    @BeforeEach
    void setUp() {
        this.postId = UUID.randomUUID();
        this.post = Post.builder()
                .title("Test Post")
                .build();

        setPostId(post, postId);
    }

    /* Post의 id를 임의로 설정 */
    private void setPostId(Post post, UUID id) {
        try {
            Field idField = Post.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(post, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("메일 보내기 테스트")
    void testSendMail() {
        List<String> emails = List.of("{자신의 이메일 넣기}");

        assertDoesNotThrow(() -> mailService.sendMail(emails, post));
    }
}
