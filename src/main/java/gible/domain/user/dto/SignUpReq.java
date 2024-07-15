package gible.domain.user.dto;

import gible.domain.user.entity.Role;
import gible.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;

public record SignUpReq(
        @NotBlank
        String nickname,
        @NotBlank
        String email,
        @NotBlank
        String name,
        @NotBlank
        String phoneNumber,
        String role
) {
    public static User toEntity(SignUpReq signUpDto) {
        return User.builder()
                .name(signUpDto.name())
                .email(signUpDto.email())
                .phoneNumber(signUpDto.phoneNumber())
                .nickname(signUpDto.nickname())
                .role(Role.valueOf(signUpDto.role()))
                .build();
    }
}
