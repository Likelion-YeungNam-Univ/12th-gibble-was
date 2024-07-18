package gible.domain.auth.dto;

public record SignInRes(
        String accessToken,
        String refreshToken
) {
    public static SignInRes of(String accessToken, String refreshToken) {
        return new SignInRes(accessToken, refreshToken);
    }
}
