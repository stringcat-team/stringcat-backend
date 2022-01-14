package com.sp.api.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class AuthReqDto {

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @ApiModel("Id Password Login")
    public static class Login {

        @ApiModelProperty(value = "사용자 이메일", example = "stringcat@gmail.com", required = true)
        @NotEmpty(message = "이메일을 입력해주세요.")
        private String email;

        @ApiModelProperty(value = "비밀번호", example = "Stringcat!", required = true)
        @NotEmpty(message = "비밀번호를 입력해주세요.")
        private String password;
    }

    @Data
    @NoArgsConstructor
    @ApiModel("Social Login")
    public static class Social {
        private String accessToken;
    }
}
