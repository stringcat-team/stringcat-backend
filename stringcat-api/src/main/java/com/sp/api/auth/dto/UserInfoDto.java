package com.sp.api.auth.dto;

import com.sp.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

public class UserInfoDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class GoogleUserInfo {
        public String socialId;
        public String email;

        public User toUser(String accessToken) {
            return new User(socialId, email);
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class KakaoUserInfo {
        public long socialId;
        public String email;

        public User toUser(String accessToken) {
            return new User(String.valueOf(socialId), email);
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class GithubUserInfo {
        public String socialId;
        public String email;

        public User toUser(String accessToken) {
            return new User(socialId, email);
        }
    }
}
