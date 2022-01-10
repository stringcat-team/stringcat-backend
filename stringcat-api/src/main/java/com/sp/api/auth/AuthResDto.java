package com.sp.api.auth;

import com.sp.domain.domain.user.Users;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthResDto {

    private final String accessToken;
    private final String refreshToken;
    private final Long id;
    private final String email;

    public AuthResDto(String accessToken, Users user) {
        this.accessToken = accessToken;
        this.id = user.getId();
        this.email = user.getEmail();
        this.refreshToken = user.getRefreshToken();
    }

    public AuthResDto(String accessToken, String refreshToken, Users user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.id = user.getId();
        this.email = user.getEmail();
    }
}