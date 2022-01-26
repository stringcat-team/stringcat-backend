package com.sp.api.auth.service;

import com.sp.api.auth.dto.AuthResDto;
import com.sp.domain.code.SocialType;
import com.sp.domain.code.UserRole;
import com.sp.domain.user.User;
import com.sp.exception.type.ErrorCode;
import com.sp.exception.type.StringcatCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class GithubClient implements ClientProxy {

    private final WebClient webClient;

    @Override
    public User getUserData(String accessToken) {
        AuthResDto.OauthRes oauthRes = webClient.get()
                .uri("https://github.com/login/oauth/access_token")
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new StringcatCustomException("Access token is unauthorized", ErrorCode.UNAUTHORIZED_EXCEPTION)))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new StringcatCustomException("Internal Server Error", ErrorCode.SERVER_EXCEPTION)))
                .bodyToMono(AuthResDto.OauthRes.class)
                .block();

        return User.builder()
                .socialId(oauthRes.getSocialId())
                .email(oauthRes.getEmail())
                .createdAt(LocalDateTime.now())
                .role(UserRole.USER)
                .socialType(SocialType.GITHUB)
                .deleted(false)
                .build();
    }

}
