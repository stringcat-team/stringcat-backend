package com.sp.api.auth.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

public class MailResDto {

    private static final String SUBJECT_AUTH_EMAIL = "[Stringcat] 이메일 인증번호 발급 안내";
    private static final String SUBJECT_PASSWORD = "[Stringcat] 임시 비밀번호 발급 안내";

    String content = "";

    @Data
    @NoArgsConstructor
    @Accessors(chain = true)
    @ApiModel("Send Mail")
    public static class MailRes {

        private String email;
        private String title;
        private String content;

    }
}
