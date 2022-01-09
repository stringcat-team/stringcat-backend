package com.sp.api.common.security;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class JwtAuthenticationResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private long id;
    private String nickname;
    private String email;
    private String socialId;
}