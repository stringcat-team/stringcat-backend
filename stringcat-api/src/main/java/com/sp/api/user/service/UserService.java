package com.sp.api.user.service;

import com.sp.api.auth.dto.AuthReqDto;
import com.sp.domain.code.SocialType;
import com.sp.domain.code.UserRole;
import com.sp.domain.user.User;
import com.sp.domain.user.UserRepository;
import com.sp.exception.type.ErrorCode;
import com.sp.exception.type.StringcatCustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public Optional<User> findByEmailAndDeletedFalse(String email) {
        return userRepository.findByEmailAndDeletedFalse(email);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getById(Long id) {
        return findById(id)
                .orElseThrow(() -> {
                    log.error("Not found entity :: userId = " + id);

                    return new StringcatCustomException("존재하지 않는 사용자입니다.", ErrorCode.NOT_FOUND);
                });
    }

    public void register(AuthReqDto.SignUp request) {
        Optional<User> user = findByEmailAndDeletedFalse(request.getEmail());

        if(!isEmpty(user)) {
            throw new StringcatCustomException("이미 존재하는 회원입니다.", ErrorCode.CONFLICT_EXCEPTION);
        }

        User newUser = User.builder()
                .email(request.getEmail())
                .socialType(SocialType.NONE)
                .socialId("normal")
                .role(UserRole.USER)
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .github(request.getGithubUrl())
                .score(0)
                .intro(request.getIntro())
                .emailFlag(true)
                .createdAt(LocalDateTime.now())
                .deleted(false).build();

        newUser = userRepository.save(newUser);
    }

    public boolean isMatchedPassword(Long id, String password) {
        User user = getById(id);

        if(passwordEncoder.matches(password, user.getPassword())) {
            return true;
        } else
            return false;
    }

    @Transactional
    public void delete(Long id) {
        User user = getById(id);

        if(user.isDeleted()) {
            throw new StringcatCustomException("이미 삭제된 사용자입니다.", ErrorCode.NOT_FOUND);
        }

        user.delete();
    }
}
