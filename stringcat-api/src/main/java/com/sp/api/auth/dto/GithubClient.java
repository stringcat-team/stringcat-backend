package com.sp.api.auth.dto;

import com.sp.api.common.exception.ApiException;
import com.sp.domain.code.SocialType;
import com.sp.domain.code.UserRole;
import com.sp.domain.user.User;
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
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new ApiException("Access token is unauthorized")))
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new ApiException("Internal Server Error")))
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
