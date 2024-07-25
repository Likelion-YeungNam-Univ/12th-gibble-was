package gible.domain.user.dto;

import gible.domain.user.entity.Role;
import gible.domain.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SignUpReq(
        @NotBlank(message = "닉네임 입력은 필수입니다.")
        String nickname,
        @NotBlank(message = "이메일 입력은 필수입니다.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        String email,
        @NotBlank(message = "이름 입력은 필수입니다.")
        String name,
        @NotBlank(message = "전화번호 입력은 필수입니다.")
        @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "전화번호 형식은 010-xxxx-xxxx이어야 합니다.")
        String phoneNumber,
        boolean emailAgree,
        String role
) {
    public static User toEntity(SignUpReq signUpDto) {
        return User.builder()
                .name(signUpDto.name())
                .email(signUpDto.email())
                .phoneNumber(signUpDto.phoneNumber())
                .nickname(signUpDto.nickname())
                .emailAgree(signUpDto.emailAgree())
                .role(Role.valueOf(signUpDto.role()))
                .build();
    }
}
