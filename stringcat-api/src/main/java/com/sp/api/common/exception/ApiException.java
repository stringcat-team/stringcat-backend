package com.sp.api.common.exception;

public class ApiException extends RuntimeException {

    private String status;
    private String message;

    public ApiException() {
        super();
    }

    public ApiException(String message) {
        super(message);
        this.status = "error";
    }

    public ApiException(String status, String message) {
        super(message);
        this.status = status;
    }
}
