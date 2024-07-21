package gible.domain.user.dto;

public record MyPageRes(
        String email,
        String nickname,
        int point
) {
    public static MyPageRes of(final String email, final String nickname, final int point) {
        return new MyPageRes(email, nickname, point);
    }
}
