package com.sp.api.advice;

import com.sp.api.common.dto.ApiResponse;
import com.sp.exception.type.StringcatCustomException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class AdviceController {

    @ExceptionHandler(StringcatCustomException.class)
    public ResponseEntity<ApiResponse<Object>> stringcatCustomException(StringcatCustomException exception) {
        return ResponseEntity
            .status(exception.getStatusCode())
            .body(ApiResponse.error(exception.getErrorCode()));
    }

    public HttpServletRequest getServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }

        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    public Long getUserId() {
        HttpServletRequest servletRequest = getServletRequest();

        if(servletRequest == null) return null;

        Object userId = servletRequest.getAttribute("ID");

        return userId != null ? (long) userId : null;
    }
}
