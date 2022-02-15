package com.sp.api.auth.service;

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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

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

    public AuthResDto.AuthRes kakaoLogin(String accessToken) {
        KakaoResDto kakaoDto = oauth2Client.getKakaoUserInfo(accessToken);
        Long socialId = kakaoDto.getId();
        String email = kakaoDto.getEmail();
        String password = socialId+ADMIN_KEY;

        User kakaoUser = userRepository.findByEmailAndDeletedFalse(email).orElse(null);
        JwtToken jwtToken = jwtTokenProvider.generateNewToken(socialId+"");

        if(kakaoUser == null) {
            String encodedPassword = passwordEncoder.encode(password);
            User.builder()
                    .socialId(socialId+"")
                    .email(email)
                    .createdAt(LocalDateTime.now())
                    .password(encodedPassword)
                    .socialType(SocialType.KAKAO)
                    .role(UserRole.USER)
                    .deleted(false)
                    .build();

            Authentication kakaoUsernamePassword = new UsernamePasswordAuthenticationToken(email, password);
            Authentication authentication = authenticationManager.authenticate(kakaoUsernamePassword);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return AuthResDto.AuthRes.builder()
                    .accessToken(jwtToken.getToken())
                    .isNewMember(Boolean.TRUE)
                    .build();
        }

        Authentication kakaoUsernamePassword = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManager.authenticate(kakaoUsernamePassword);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return AuthResDto.AuthRes.builder()
                .accessToken(jwtToken.getToken())
                .isNewMember(Boolean.FALSE)
                .build();
    }

    public AuthResDto.AuthRes googleLogin(String accessToken) {
        GoogleResDto googleDto = oauth2Client.getGoogleUserInfo(accessToken);

        String socialId = googleDto.getSub();
        String email = googleDto.getEmail();
        String password = socialId + ADMIN_KEY;

        User googleUser = userRepository.findByEmailAndDeletedFalse(email).orElse(null);
        JwtToken jwtToken = jwtTokenProvider.generateNewToken(socialId);

        if (googleUser == null) {
            String encodedPassword = passwordEncoder.encode(password);
            User.builder()
                    .socialId(socialId)
                    .email(email)
                    .socialType(SocialType.GOOGLE)
                    .createdAt(LocalDateTime.now())
                    .password(encodedPassword)
                    .role(UserRole.USER)
                    .deleted(false)
                    .build();

            Authentication googleUsernamePassword = new UsernamePasswordAuthenticationToken(email, password);
            Authentication authentication = authenticationManager.authenticate(googleUsernamePassword);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return AuthResDto.AuthRes.builder()
                    .accessToken(jwtToken.getToken())
                    .isNewMember(Boolean.TRUE)
                    .build();
        }

        Authentication kakaoUsernamePassword = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManager.authenticate(kakaoUsernamePassword);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return AuthResDto.AuthRes.builder()
                .accessToken(jwtToken.getToken())
                .isNewMember(Boolean.FALSE)
                .build();
    }

    public AuthResDto.AuthRes githubLogin(String accessToken) {
        GithubResDto githubDto = oauth2Client.getGithubUserInfo(accessToken);

        String socialId = githubDto.getId();
        String email = githubDto.getEmail();
        String password = socialId + ADMIN_KEY;

        User githubUser = userRepository.findByEmailAndDeletedFalse(email).orElse(null);
        JwtToken jwtToken = jwtTokenProvider.generateNewToken(socialId);

        if (githubUser == null) {
            String encodedPassword = passwordEncoder.encode(password);
            User.builder()
                    .socialId(socialId)
                    .email(email)
                    .socialType(SocialType.GITHUB)
                    .createdAt(LocalDateTime.now())
                    .password(encodedPassword)
                    .role(UserRole.USER)
                    .deleted(false)
                    .build();

            Authentication githubUsernamePassword = new UsernamePasswordAuthenticationToken(email, password);
            Authentication authentication = authenticationManager.authenticate(githubUsernamePassword);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return AuthResDto.AuthRes.builder()
                    .accessToken(jwtToken.getToken())
                    .isNewMember(Boolean.TRUE)
                    .build();
        }

        Authentication kakaoUsernamePassword = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManager.authenticate(kakaoUsernamePassword);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return AuthResDto.AuthRes.builder()
                .accessToken(jwtToken.getToken())
                .isNewMember(Boolean.FALSE)
                .build();
    }

    public AuthResDto.AuthRes updateToken(JwtToken jwtToken) {
        Claims claims = jwtToken.getTokenClaims();

        if(claims == null) {
            return null;
        }

        String socialId = claims.getSubject();

        JwtToken newToken = jwtTokenProvider.generateNewToken(socialId);

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

}
