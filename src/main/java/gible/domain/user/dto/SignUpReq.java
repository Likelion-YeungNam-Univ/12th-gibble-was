package gible.domain.user.dto;

import gible.domain.user.entity.Role;
import gible.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;

public record SignUpReq(
        @NotBlank(message = "닉네임 입력은 필수입니다.")
        String nickname,
        @NotBlank(message = "이메일 입력은 필수입니다.")
        String email,
        @NotBlank(message = "이름 입력은 필수입니다.")
        String name,
        @NotBlank(message = "전화번호 입력은 필수입니다.")
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
