package com.sp.api.user.service;

import com.sp.api.auth.dto.AuthReqDto;
import com.sp.api.common.exception.ApiException;
import com.sp.api.grade.service.GradeService;
import com.sp.api.skill.dto.SkillResDto;
import com.sp.api.user.dto.UserResDto;
import com.sp.domain.code.SocialType;
import com.sp.domain.code.UserRole;
import com.sp.domain.skill.Skill;
import com.sp.domain.user.User;
import com.sp.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final GradeService gradeService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public Optional<User> findByEmailAndDeletedFalse(String email) {
        return userRepository.findByEmailAndDeletedFalse(email);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void register(AuthReqDto.SignUp request) {
        Optional<User> user = findByEmailAndDeletedFalse(request.getEmail());

        if(!isEmpty(user)) {
            throw new ApiException("이미 존재하는 회원입니다.");
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

}
