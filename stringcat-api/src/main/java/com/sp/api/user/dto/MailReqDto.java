package com.sp.api.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

public class MailReqDto {

    @Data
    @NoArgsConstructor
    @Accessors(chain = true)
    @ApiModel("Mail to")
    public static class MailTo {

        @ApiModelProperty(value = "사용자 이메일 주소", example = "jennachoi27@gmail.com", required = true)
        @NotEmpty(message = "수신자 이메일을 입력해주세요.")
        private String email;

        @ApiModelProperty(value = "메일 제목", example = "[stringcat] 회원가입을 위한 이메일 인증", required = true)
        @NotEmpty(message = "메일 제목은 필수입니다.")
        private String title;

        @ApiModelProperty(value = "메일 내용", example = "sd45fy", required = true)
        @NotEmpty(message = "내용을 넣어주세요.")
        private String content;

    }
}
