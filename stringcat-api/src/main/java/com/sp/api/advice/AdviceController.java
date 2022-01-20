package com.sp.api.advice;

import com.sp.api.common.dto.ApiResponse;
import com.sp.exception.type.StringcatCustomException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AdviceController {

    @ExceptionHandler(StringcatCustomException.class)
    public ResponseEntity<ApiResponse<Object>> stringcatCustomException(StringcatCustomException exception) {
        return ResponseEntity
            .status(exception.getStatusCode())
            .body(ApiResponse.error(exception.getErrorCode()));
    }
}
