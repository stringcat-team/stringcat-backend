package com.sp.api;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.sp.api.common.exception.CriticalException;
import com.sp.api.common.utils.ApiResponse;
import com.sp.api.common.exception.ApiException;
import com.sp.domain.domain.user.UserRepository;
import com.sp.domain.domain.user.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
@CrossOrigin
public class ParentController {

    protected static final String APPLICATION_JSON = "application/json; charset=UTF-8";

    @Value("local_dev")
    String properties;

    @Autowired
    protected UserRepository _userRepository;

    public HttpServletRequest getServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }

        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    public Long getUserId() {
        HttpServletRequest servletRequest = getServletRequest();

        if (servletRequest == null) return null;
        Object userId = servletRequest.getAttribute("ID");

        return userId != null ? (long) userId : null;
    }

    public Users getUser() {
        return _userRepository.findById(getUserId())
                .orElseThrow(() -> new ApiException("Can't find this info."));
    }

    public void checkSameUserLoginAndRequest(long userId) {
        checkSameUserLoginAndRequest(userId, "It doesn't match with any information.");
    }

    public void checkSameUserLoginAndRequest(long userId, String message) {
        if (getUserId() == null || getUserId() != userId) throw new ApiException(message);
    }

    public Optional<Users> getOptionalMember() {
        return _userRepository.findById(getUserId());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ApiResponse processValidationError(MethodArgumentNotValidException ex) {
        FieldError fieldError = (FieldError) ex.getBindingResult().getFieldErrors().get(0);
        return ApiResponse.error(fieldError.getDefaultMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({AuthenticationException.class, TokenExpiredException.class, AccessDeniedException.class})
    public ApiResponse handleAuthException(Exception e) {
        log.error("ApiException ::: {}", e.getMessage());
        e.printStackTrace();
        return ApiResponse.error("권한이 없거나, 인증에 실패 했습니다.");
    }

    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ExceptionHandler({ApiException.class})
    public ApiResponse handleApiException(Exception e) {
        log.error("ApiException ::: {}", e.getMessage());
        e.printStackTrace();
        return ApiResponse.error(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
//    @ExceptionHandler({NotFoundException.class})
    public ApiResponse handleNotFoundException(Exception e) {
        log.error("NotFoundException ::: {}", e.getMessage());
        e.printStackTrace();
        return ApiResponse.error(e.getMessage());
    }

    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ExceptionHandler({CriticalException.class})
    public ApiResponse handleCriticalException(CriticalException e) {
        log.error("ApiException ::: {}", e.getMessage());
        e.printStackTrace();
        return ApiResponse.error(e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    public ApiResponse handleException(Exception e) {
        log.error("Exception ::: {}", e.getMessage());
        e.printStackTrace();
        return ApiResponse.error(e.getMessage());
    }

}
