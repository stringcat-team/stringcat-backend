package com.sp.api.auth.service;

import com.sp.api.auth.dto.*;
import com.sp.api.auth.security.jwt.JwtToken;
import com.sp.api.auth.security.jwt.JwtTokenProvider;
import com.sp.domain.user.User;
import com.sp.domain.user.UserQuerydslRepositoryImpl;
import com.sp.exception.type.ErrorCode;
import com.sp.exception.type.StringcatCustomException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserQuerydslRepositoryImpl userQuerydslRepository;

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

    public Long getUserId(String token) {
        JwtToken jwtToken = jwtTokenProvider.convertStringToJwtToken(token);

        Claims claims = jwtToken.getTokenClaims();

        if(claims == null) {
            return null;
        }

        try {
            User user = userQuerydslRepository.findBySocialId(claims.getSubject());
            return user.getId();
        } catch (NullPointerException ex) {
            throw new StringcatCustomException("사용자가 존재하지 않습니다.", ErrorCode.NOT_FOUND_USER);
        }
    }

}
