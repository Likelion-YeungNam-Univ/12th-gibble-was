package gible.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record RenewTokenReq (
        @NotBlank(message = "리프레시 토큰은 필수 작성 항목입니다.")
        String RefreshToken
){
}
