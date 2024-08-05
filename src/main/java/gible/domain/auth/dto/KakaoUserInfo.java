package gible.domain.auth.dto;

public record KakaoUserInfo(
        String email

) {
    public static KakaoUserInfo of(String email) {
        return new KakaoUserInfo(email);
    }
}
