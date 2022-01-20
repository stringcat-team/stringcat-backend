package com.sp.api.user.dto;

import com.sp.domain.code.SocialType;
import com.sp.domain.code.UserRole;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class UserResDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel("Basic UserInfo")
    public static class UserInfo {
        private Long id;
        private Long gradeId;
        private UserRole role;
        private String socialId;
        private SocialType socialType;
        private String email;
        private String nickname;
        private String github;
        private String bio;
        private String intro;
        private String imageUrl;
        private LocalDateTime createdAt;
    }
}
