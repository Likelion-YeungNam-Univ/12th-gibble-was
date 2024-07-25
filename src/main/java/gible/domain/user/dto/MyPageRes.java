package gible.domain.user.dto;

public record MyPageRes(
        String email,
        String nickname
) {
    public static MyPageRes of(final String email, final String nickname) {
        return new MyPageRes(email, nickname);
    }
}
