package com.sp.api.common.exception;

import lombok.Data;

@Data
public class CriticalException extends RuntimeException {

    private String status;
    private String message;
    private String title;

    public CriticalException() {
        super();
    }

    public CriticalException(String message, String title) {
        super(message);
        this.status = "error";
        this.message = message;
        this.title = title;
    }

    public CriticalException(String status, String message, String title) {
        super(message);
        this.status = status;
        this.message = message;
        this.title = title;
    }
}
