package com.sp.api.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ApiException extends RuntimeException {

    private String status;
    private String message;

    public ApiException() {
        super("Exception!");
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(String status, String message) {
        super(message);
        this.status = status;
    }
}
