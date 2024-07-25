package gible.domain.mail.service;

import gible.domain.post.entity.Post;
import gible.exception.CustomException;
import gible.exception.error.ErrorType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static gible.domain.mail.util.MailProperties.MAIL_SUBJECT;

@RequiredArgsConstructor
@Service
public class MailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String emailUsername;

    @Value("${web.post-url}")
    private String postURL;

    /* 메일 보내기 */
    @Async
    public void sendMail(List<String> emails, Post post) {
        try {
            String text = getText(post);
            MimeMessage mimeMessage = getMessage(emails, text);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new CustomException(ErrorType.INTERNAL_SERVER_ERROR);
        }
    }

    /* 메일 메시지 구성 */
    private MimeMessage getMessage(List<String> emails, String text) throws MessagingException, UnsupportedEncodingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setBcc(emails.toArray(new String[0]));
        helper.setSubject(MAIL_SUBJECT);
        helper.setText(text, true);
        helper.setFrom(new InternetAddress(emailUsername,"기블","UTF-8"));

        return mimeMessage;
    }

    /* 메일 내용 작성 */
    private String getText(Post post) {
        return  "제목: " + post.getTitle() + "<br>" +
                "<p><a href=" + postURL + post.getId() + ">게시글 링크</a></p>";
    }
}
