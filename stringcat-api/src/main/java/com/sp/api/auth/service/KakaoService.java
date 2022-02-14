package com.sp.api.auth.service;

import com.sp.api.auth.dto.AuthResDto;
import com.sp.api.auth.security.jwt.JwtToken;
import com.sp.api.auth.security.jwt.JwtTokenProvider;
import com.sp.domain.user.User;
import com.sp.domain.user.UserQuerydslRepositoryImpl;
import com.sp.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {

    private final Oauth2Client kakaoClient;
    private final UserQuerydslRepositoryImpl userQuerydslRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
//
//    public AuthResDto.AuthRes login(AuthReqDto.Social request) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        headers.set("Authorization", "Bearer " + request.getAccessToken());
//        HttpEntity<String> entity = new HttpEntity<String>("", headers);
//        ResponseEntity<String> response = restTemplate.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.POST, entity, String.class);
//
//        User kakaoUser = kakaoClient.getUserData(request.getAccessToken());
//        String socialId = kakaoUser.getSocialId();
//
//        log.info(socialId + "가져왔는지");
//
//        return getAuthRes(kakaoUser, socialId, userQuerydslRepository, jwtTokenProvider, userRepository);
//    }

    static AuthResDto.AuthRes getAuthRes(User kakaoUser, String socialId, UserQuerydslRepositoryImpl userQuerydslRepository, JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
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

//    @Transactional
//    public AuthResDto.AuthRes login(AuthReqDto.Social request) {
//        User kakaoUser = kakaoClient.getUserData(request.getAccessToken());
//        String socialId = kakaoUser.getSocialId();
//
//        log.info(socialId + "가져왔는지");
//
//        User user = userQuerydslRepository.findBySocialId(socialId);
//
//        JwtToken jwtToken = jwtTokenProvider.generateNewToken(socialId);
//
//        if(user == null) {
//            userRepository.save(kakaoUser);
//            return AuthResDto.AuthRes.builder()
//                    .accessToken(jwtToken.getToken())
//                    .isNewMember(Boolean.TRUE)
//                    .build();
//        }
//
//        return AuthResDto.AuthRes.builder()
//                .accessToken(jwtToken.getToken())
//                .isNewMember(Boolean.FALSE)
//                .build();
//    }
}
