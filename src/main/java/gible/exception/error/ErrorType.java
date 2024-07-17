package gible.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {

    // 인증이 되어 있지 않을 때.
    UNAUTHORIZED(401, "접근 권한이 없습니다."),
    ACCESS_TOKEN_EXPIRED(401, "Access Token이 만료되었습니다."),
    INVALID_ACCESS_TOKEN(401, "Access Token이 잘못되었습니다."),
    SOCIAL_LOGIN_FAILED(401, "소셜로그인 오류"),
    // AccessToken 관련 오류
    BLACKLIST_ACCESS_TOKEN(401, "접근 불가한 AccessToken입니다."),

    // RefreshToken 인증 중 오류
    REFRESH_TOKEN_NOT_FOUND(404, "RefreshToken이 존재하지 않습니다."),
    REFRESH_TOKEN_EXPIRED(404, "RefreshToken이 만료되었습니다."),
    TOKEN_NOT_FOUND(404, "Token이 존재하지 않습니다."),

    // 존재하지 않는 값을 보낼 때.
    USER_NOT_FOUND(404, "존재하지 않는 회원입니다."),
    POST_NOT_FOUND(404, "존재하지 않는 글입니다."),
    EVENT_NOT_FOUND(404, "존재하지 않는 이벤트입니다."),
    DONATE_NOT_FOUND(404, "기부한 이력이 없습니다."),
    PARTICIPATE_NOT_FOUND(404, "해당 이벤트에 참여한 이력이 없습니다."),

    // 이미 존재하는 값을 보냈을 때.
    ALREADY_PARTICIPATE_POST(409, "이미 해당 이벤트에 참여하였습니다."),

    // 서버 에러
    INTERNAL_SERVER_ERROR(500, "서버 에러입니다. 서버 팀에 연락주세요.");

    private final int status;
    private final String message;
}
