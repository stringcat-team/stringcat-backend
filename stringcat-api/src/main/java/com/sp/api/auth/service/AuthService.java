package com.sp.api.auth.service;

import com.sp.api.auth.dto.*;
import com.sp.api.auth.security.jwt.JwtToken;
import com.sp.api.auth.security.jwt.JwtTokenProvider;
import com.sp.domain.user.User;
import com.sp.domain.user.UserQuerydslRepository;
import com.sp.domain.user.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AuthService {

    @Autowired GoogleClient googleClient;
    @Autowired GithubClient githubClient;
    @Autowired KakaoClient kakaoClient;
    @Autowired JwtTokenProvider jwtTokenProvider;
    @Autowired UserRepository userRepository;
    @Autowired UserQuerydslRepository userQuerydslRepository;

    @Transactional
    public AuthResDto.AuthRes kakaoLogin(AuthReqDto.Social request) {
        User kakaoUser = kakaoClient.getUserData(request.getAccessToken());
        String socialId = kakaoUser.getSocialId();
        User user = userQuerydslRepository.findBySocialId(socialId);

        JwtToken token = jwtTokenProvider.generateNewToken(socialId);

        if(user == null) {
            userRepository.save(kakaoUser);

            return new AuthResDto.AuthRes()
                    .setAccessToken(token.getToken())
                    .setNewMember(true);
        } else {
            return new AuthResDto.AuthRes()
                    .setAccessToken(token.getToken())
                    .setNewMember(false);
        }
    }

    @Transactional
    public AuthResDto.AuthRes googleLogin(AuthReqDto.Social request) {
        User googleUser = googleClient.getUserData(request.getAccessToken());
        String socialId = googleUser.getSocialId();
        User user = userQuerydslRepository.findBySocialId(socialId);

        JwtToken token = jwtTokenProvider.generateNewToken(socialId);

        if(user == null) {
            userRepository.save(googleUser);

            return new AuthResDto.AuthRes()
                    .setAccessToken(token.getToken())
                    .setNewMember(true);
        } else {
            return new AuthResDto.AuthRes()
                    .setAccessToken(token.getToken())
                    .setNewMember(false);
        }
    }

    @Transactional
    public AuthResDto.AuthRes githubLogin(AuthReqDto.Social request) {
        User githubUser = githubClient.getUserData(request.getAccessToken());
        String socialId = githubUser.getSocialId();
        User user = userQuerydslRepository.findBySocialId(socialId);

        JwtToken token = jwtTokenProvider.generateNewToken(socialId);

        if(user == null) {
            userRepository.save(githubUser);

            return new AuthResDto.AuthRes()
                    .setAccessToken(token.getToken())
                    .setNewMember(true);
        } else {
            return new AuthResDto.AuthRes()
                    .setAccessToken(token.getToken())
                    .setNewMember(false);
        }
    }

    public AuthResDto.AuthRes updateToken(JwtToken jwtToken) {
        Claims claims = jwtToken.getTokenClaims();

        if(claims == null) {
            return null;
        }

        String socialId = claims.getSubject();

        JwtToken newToken = jwtTokenProvider.generateNewToken(socialId);

        return new AuthResDto.AuthRes()
                .setAccessToken(newToken.getToken())
                .setNewMember(false);
    }

}
