package com.sp.api.user.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

public class MailResDto {

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
