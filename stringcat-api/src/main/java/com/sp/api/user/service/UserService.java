package com.sp.api.user.service;

import com.sp.domain.user.User;
import com.sp.domain.user.UserQuerydslRepositoryImpl;
import com.sp.domain.user.UserRepository;
import com.sp.exception.type.ErrorCode;
import com.sp.exception.type.StringcatCustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserQuerydslRepositoryImpl userQuerydslRepository;

    public Optional<User> findByEmailAndDeletedFalse(String email) {
        return userRepository.findByEmailAndDeletedFalse(email);
    }
    public long getUserId() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        if(requestAttributes == null) {
            return -1;
        }

        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        Object userId = request.getAttribute("ID");

        return isNotNull(userId) ? (long) userId : -1;
    }

    public boolean isNull(Object o) {
        return Objects.isNull(o);
    }

    public boolean isNotNull(Object o) {
        return !isNull(o);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User getById(Long id) {
        return findById(id)
                .orElseThrow(() -> {
                    log.error("Not found entity :: userId = " + id);

                    return new StringcatCustomException("존재하지 않는 사용자입니다.", ErrorCode.NOT_FOUND);
                });
    }

    public boolean isMatchedPassword(Long id, String password) {
        User user = getById(id);

        if(passwordEncoder.matches(password, user.getPassword())) {
            return true;
        } else
            throw new StringcatCustomException("사용자의 비밀번호가 일치하지 않습니다", ErrorCode.FORBIDDEN_EXCEPTION);

    }

    @Transactional
    public void delete(Long id) {
        User user = getById(id);

        if(user.isDeleted()) {
            throw new StringcatCustomException("이미 삭제된 사용자입니다.", ErrorCode.NOT_FOUND);
        }

        user.delete();
    }

    public boolean checkEmail(String email) {
        User user = userQuerydslRepository.findByEmail(email);

        return user != null && !user.getSocialId().equals("NOT_SUB");
    }
}
