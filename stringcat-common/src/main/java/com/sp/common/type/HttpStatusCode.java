package com.sp.common.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum HttpStatusCode {
    OK(200, "성공"),
    BAD_REQUEST(400, "잘못된 요청"),
    UNAUTHORIZED(401, "인증 필요"),
    FORBIDDEN(403, "금지됨"),
    NOT_FOUND(404, "찾을 수 없음"),
    METHOD_NOT_ALLOWED(405, "허용하지 않는 HTTP 메서드"),
    NOT_ACCEPTABLE(406, "Not acceptable"),
    CONFLICT(409, "중복된 리소스"),
    UNSUPPORTED_MEDIA_TYPE(415, "지원하지 않는 미디어 타입"),
    INTERNAL_SERVER(500, "서버 문제"),
    BAD_GATEWAY(502, "잘못된 응답"),
    SERVICE_UNAVAILABLE(503, "요청을 처리할 수 없습니다.");

    private final int code;
    private final String desc;
}
