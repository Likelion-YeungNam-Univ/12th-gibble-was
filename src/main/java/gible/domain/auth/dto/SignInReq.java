package gible.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record SignInReq(
        @NotBlank
        String code
        ) {
}
