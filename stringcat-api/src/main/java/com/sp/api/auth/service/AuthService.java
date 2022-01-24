package com.sp.api.auth.service;

import com.sp.api.auth.dto.*;
import com.sp.api.auth.security.jwt.JwtToken;
import com.sp.api.auth.security.jwt.JwtTokenProvider;
import com.sp.domain.user.User;
import com.sp.domain.user.UserQuerydslRepository;
import com.sp.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final GoogleClient googleClient;
    private final GithubClient githubClient;
    private final KakaoClient kakaoClient;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final UserQuerydslRepository userQuerydslRepository;

    @Transactional
    public AuthResDto.AuthRes googleLogin(AuthReqDto.Social request) {
        return getAuthRes(googleClient.getUserData(request.getAccessToken()), request);
    }

    @Transactional
    public AuthResDto.AuthRes kakaoLogin(AuthReqDto.Social request) {
        return getAuthRes(kakaoClient.getUserData(request.getAccessToken()), request);
    }

    @Transactional
    public AuthResDto.AuthRes githubLogin(AuthReqDto.Social request) {
        return getAuthRes(githubClient.getUserData(request.getAccessToken()), request);
    }

    private AuthResDto.AuthRes getAuthRes(User userData, AuthReqDto.Social request) {
        User socialUser = userData;
        String socialId = socialUser.getSocialId();
        User user = userQuerydslRepository.findBySocialId(socialId);

        JwtToken jwtToken = jwtTokenProvider.generateToken(socialId, user.getRole());

        if(user == null) {
            userRepository.save(socialUser);
            return new AuthResDto.AuthRes()
                    .setAccessToken(jwtToken.getToken())
                    .setNewMember(true);
        }

        return new AuthResDto.AuthRes()
                .setAccessToken(jwtToken.getToken())
                .setNewMember(false);
    }

}
