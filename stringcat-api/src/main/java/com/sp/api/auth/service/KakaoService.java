package com.sp.api.auth.service;

import com.sp.api.auth.dto.AuthReqDto;
import com.sp.api.auth.dto.AuthResDto;
import com.sp.api.auth.security.jwt.JwtToken;
import com.sp.api.auth.security.jwt.JwtTokenProvider;
import com.sp.domain.user.User;
import com.sp.domain.user.UserQuerydslRepositoryImpl;
import com.sp.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KakaoService {

    private final KakaoClient kakaoClient;
    private final UserQuerydslRepositoryImpl userQuerydslRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private UserRepository userRepository;

    @Transactional
    public AuthResDto.AuthRes login(AuthReqDto.Social request) {
        User kakaoUser = kakaoClient.getUserData(request.getAccessToken());
        String socialId = kakaoUser.getSocialId();
        User user = userQuerydslRepository.findBySocialId(socialId);

        JwtToken jwtToken = jwtTokenProvider.generateNewToken(socialId);

        if(user == null) {
            userRepository.save(kakaoUser);
            return AuthResDto.AuthRes.builder()
                    .accessToken(jwtToken.getToken())
                    .isNewMember(Boolean.TRUE)
                    .build();
        }

        return AuthResDto.AuthRes.builder()
                .accessToken(jwtToken.getToken())
                .isNewMember(Boolean.FALSE)
                .build();
    }
}
