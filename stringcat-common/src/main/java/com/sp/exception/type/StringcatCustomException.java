package com.sp.exception.type;

import lombok.Getter;

@Getter
public class StringcatCustomException extends RuntimeException {

    private final ErrorCode errorCode;

    public StringcatCustomException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getStatusCode() {
        return errorCode.getStatusCode().getCode();
    }
}
