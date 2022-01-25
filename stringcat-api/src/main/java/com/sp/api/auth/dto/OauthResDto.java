package com.sp.api.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class OauthResDto {

    @ToString
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GoogleAccount {
        private String socialId;
        private String email;
    }

    @ToString
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KakaoAccount {
        private String socialId;
        private String email;
    }


}