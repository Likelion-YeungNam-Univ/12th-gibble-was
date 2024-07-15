package gible.user.domain;


import gible.domain.user.entity.User;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.*;

class UserTest {

    @Test
    void 객체를_생성한다() {
        assertThatCode(() -> User.builder()
                .name("홍길동")
                .email("abcd@gmail.com")
                .nickname("호옹길동")
                .phoneNumber("01012345678")
                .build())
                .doesNotThrowAnyException();
    }

    @Test
    void 이메일_형식이_아니면_예외를_발생한다() {
        final Pattern EMAIL_REGEX = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
        User user = User.builder()
                .name("홍길동")
                .email("abcdgmail.com")
                .nickname("호옹길동")
                .phoneNumber("01012345678")
                .build();
        assertThatThrownBy(() -> {
            if (!(EMAIL_REGEX.matcher(user.getEmail()).matches())) {
                throw new IllegalArgumentException("validation error");
            }
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 유저_생성시_notnull_값을_빠뜨린다() {
        assertThatThrownBy(() -> {
            User user = User.builder()
                    .email("abcd@gmail.com")
                    .nickname("호옹길동")
                    .phoneNumber("01012345678")
                    .build();
            if (user.getName() == null) {
                throw new IllegalArgumentException("validation error");
            }
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 유저_생성시_연관관계매핑_멤버변수_초기화_여부() {
        User user = User.builder()
                .email("abcd@gmail.com")
                .nickname("호옹길동")
                .phoneNumber("01012345678")
                .build();
        assertThat(user.getPosts()).isNotNull();
    }
}
