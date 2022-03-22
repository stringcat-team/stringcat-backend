package com.sp.api.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sp.api.auth.dto.*;
import com.sp.api.auth.security.jwt.JwtToken;
import com.sp.api.auth.security.jwt.JwtTokenProvider;
import com.sp.domain.code.SocialType;
import com.sp.domain.code.UserRole;
import com.sp.domain.user.User;
import com.sp.domain.user.UserQuerydslRepositoryImpl;
import com.sp.domain.user.UserRepository;
import com.sp.exception.type.ErrorCode;
import com.sp.exception.type.StringcatCustomException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final Oauth2Client oauth2Client;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserQuerydslRepositoryImpl userQuerydslRepository;


    private static final String ADMIN_KEY = "c3RyaW5nY2F0YWRtaW5rZXkNCg==";

    public Optional<User> findByEmailAndDeletedFalse(String email) {
        return userRepository.findByEmailAndDeletedFalse(email);
    }

    public AuthResDto.AuthRes updateToken(JwtToken jwtToken) {
        Claims claims = jwtToken.getTokenClaims();

        if(claims == null) {
            return null;
        }

        String socialId = claims.getSubject();

        JwtToken newToken = jwtTokenProvider.generateSocialToken(socialId);

        return AuthResDto.AuthRes.builder()
                .accessToken(newToken.getToken())
                .build();
    }

    public void register(AuthReqDto.SignUp request) {
        Optional<User> user = findByEmailAndDeletedFalse(request.getEmail());

        if(!isEmpty(user)) {
            throw new StringcatCustomException("이미 존재하는 회원입니다.", ErrorCode.CONFLICT_EXCEPTION);
        }

        if(user.isPresent() && request.getEmail().equals(user.get().getEmail())) {
            throw new StringcatCustomException("이미 사용중인 이메일입니다.", ErrorCode.CONFLICT_EXCEPTION);
        }

        User newUser = User.builder()
                .email(request.getEmail())
                .socialType(SocialType.NONE)
                .socialId("NOT_SUB")
                .role(UserRole.USER)
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .github(request.getGithubUrl())
                .score(0)
                .intro(request.getIntro())
                .emailFlag(true)
                .createdAt(LocalDateTime.now())
                .deleted(false).build();

        userRepository.save(newUser);
    }

    public String createToken(AuthReqDto.Login request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    throw new StringcatCustomException("존재하지 않는 회원입니다.", ErrorCode.NOT_FOUND_USER);
                });

        return jwtTokenProvider.generateToken(user.getEmail());

    }

}
