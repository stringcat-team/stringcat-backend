package com.sp.api.auth.dto;

import lombok.*;
import lombok.experimental.Accessors;

public class AuthResDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class AuthRes {
        private String accessToken;
        private Boolean isNewMember;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class OauthRes {
        private String email;
        private String socialId;
    }

}
