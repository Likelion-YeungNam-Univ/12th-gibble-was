package gible.domain.user.dto;

public record UserInfoRes(
        String name,
        String phoneNumber
) {
    public static UserInfoRes of(final String name, final String phoneNumber) {
        return new UserInfoRes(name, phoneNumber);
    }
}
