package gible.domain.user.dto;

import gible.domain.user.entity.User;

public record MyPageRes(
        String email,
        String nickname,
        String name,
        String phoneNumber
) {
    public static MyPageRes fromEntity(User user) {
        return new MyPageRes(user.getEmail(), user.getNickname(), user.getName(), user.getPhoneNumber());
    }
}
