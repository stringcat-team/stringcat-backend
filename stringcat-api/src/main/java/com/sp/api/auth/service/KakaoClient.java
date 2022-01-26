package com.sp.api.auth.service;

import com.sp.api.auth.dto.AuthResDto;
import com.sp.api.common.exception.ApiException;
import com.sp.domain.code.SocialType;
import com.sp.domain.code.UserRole;
import com.sp.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoClient implements ClientProxy {

    private final WebClient webClient;

    @Override
    public User getUserData(String accessToken) {
        AuthResDto.OauthRes oauthRes = webClient.get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .headers(httpHeaders -> httpHeaders.setBearerAuth(accessToken))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new ApiException("Access token is unauthorized")))
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new ApiException("Internal Server Error")))
                .bodyToMono(AuthResDto.OauthRes.class)
                .block();

        return User.builder()
                .socialId(oauthRes.getSocialId())
                .email(oauthRes.getEmail())
                .createdAt(LocalDateTime.now())
                .role(UserRole.USER)
                .socialType(SocialType.KAKAO)
                .deleted(false)
                .build();

    }
}
