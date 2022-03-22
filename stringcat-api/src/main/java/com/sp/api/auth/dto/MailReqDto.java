package com.sp.api.auth.dto;

import com.sp.domain.code.EmailType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class MailReqDto {

    @Data
    @NoArgsConstructor
    @Accessors(chain = true)
    @ApiModel("Mail to")
    public static class MailTo {

        @ApiModelProperty(value = "사용자 이메일 주소", example = "jennachoi27@gmail.com", required = true)
        @NotEmpty(message = "수신자 이메일을 입력해주세요.")
        private String email;

        @ApiModelProperty(value = "메일 타입", example = "VERIFIER", required = true)
        @NotNull(message = "비밀번호 찾기 시: PASSWORD_SENDER, 회원가입 인증시: VERIFIER")
        private EmailType type;

        @ApiModelProperty(value = "메일 타입", example = "VERIFIER", required = true)
        @NotNull(message = "비밀번호 찾기 시: PASSWORD_SENDER, 회원가입 인증시: VERIFIER")
        private String code;

    }

    @Data
    @NoArgsConstructor
    @Accessors(chain = true)
    @ApiModel("Mail to")
    public static class AuthCode {

        @ApiModelProperty(value = "인증 코드", example = "xp234a", required = true)
        @NotEmpty(message = "이메일로 수신한 인증코드를 입력해주세요.")
        private String authCode;

    }
}
