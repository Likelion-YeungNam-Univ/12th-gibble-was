package gible.domain.auth.dto;

public record KakaoUserInfo(
        String email,
        String name,
        String phoneNumber
) {
    public static KakaoUserInfo from(String email, String name, String phoneNumber) {
        return new KakaoUserInfo(email, name, phoneNumber);
    }
}
