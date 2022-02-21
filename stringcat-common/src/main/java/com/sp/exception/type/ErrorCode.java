package com.sp.exception.type;

import static com.sp.common.type.HttpStatusCode.*;

import com.sp.common.type.HttpStatusCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {

    // 400 Bad Request
    VALIDATION_EXCEPTION(BAD_REQUEST, "VA01", "잘못된 요청입니다."),
    INVALID_REFRESH_TOKEN(BAD_REQUEST, "VA02", "리프레시 토큰이 유효하지 않습니다."),
    MISMATCH_REFRESH_TOKEN(BAD_REQUEST, "VA03", "리프레시 토큰의 유저 정보가 일치하지 않습니다."),
    INVALID_FILE_NAME(BAD_REQUEST, "VA04", "업로드 한 파일의 형식이 잘못되었습니다."),
    EXISTS_ANSWER(BAD_REQUEST, "VA05", "이미 답변을 등록했습니다."),
    SELF_WRITE(BAD_REQUEST, "VA06", "자신이 작성한 질문에 답변할 수 없습니다."),

    // 401 UnAuthorized
    UNAUTHORIZED_EXCEPTION(UNAUTHORIZED, "UA01", "다시 인증해주세요."),
    MALFORMED_JWT_TOKEN(UNAUTHORIZED, "UA02", "잘못된 형식의 토큰입니다."),
    INVALID_AUTH_TOKEN(UNAUTHORIZED, "VA03", "권한 정보가 없는 토큰입니다."),
    UNAUTHORIZED_MEMBER(UNAUTHORIZED, "VA04", "현재 내 계정 정보가 존재하지 않습니다."),

    // 403 Forbidden
    FORBIDDEN_EXCEPTION(FORBIDDEN, "FB01", "허용되지 않는 요청입니다."),
    MISMATCH_WRITER(FORBIDDEN, "FB02", "작성자가 아닙니다."),

    // 404 Not Found
    NOT_FOUND(HttpStatusCode.NOT_FOUND, "NF01", "존재하지 않습니다."),
    NOT_FOUNT_QUESTION(HttpStatusCode.NOT_FOUND, "NF02", "존재하지 않는 질문입니다."),
    NOT_FOUND_USER(HttpStatusCode.NOT_FOUND, "NF03", "존재하지 않는 사용자입니다."),
    FAILED_TO_UPLOAD_FILE(HttpStatusCode.NOT_FOUND, "NF04", "파일 업로드에 실패하였습니다."),

    // 409 Conflict
    CONFLICT_EXCEPTION(CONFLICT,  "CF01","이미 존재합니다."),

    // 500 Server Exception
    SERVER_EXCEPTION(INTERNAL_SERVER, "SE01", "예상치 못한 에러가 발생했습니다.\n잠시 후 다시 시도해주세요. "),

    // 502 Bad Gateway
    BAD_GATEWAY_EXCEPTION(BAD_GATEWAY, "BG01", "일시적인 에러가 발생했습니다.\n잠시 후 다시 시도해주세요."),

    // 503 Server UnAvailable
    SERVER_UNAVAILABLE_EXCEPTION(SERVICE_UNAVAILABLE,  "SU01","서버가 점검중 입니다.\n잠시 후 다시 시도해주세요.");


    private final HttpStatusCode statusCode;
    private final String code;
    private final String message;
}
