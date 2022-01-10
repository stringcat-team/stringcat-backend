package com.sp.api.common.exception;

import com.sp.common.type.HttpStatusCode;
import com.sp.exception.type.ErrorCode;
import lombok.Data;

@Data
public class ApiException extends RuntimeException {

    private final ErrorCode errorCode;

}
