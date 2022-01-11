package com.sp.api.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

public class AuthDto {

    public static class SignUp {

    }

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @ApiModel("AuthGeneralLoginForm")
    public static class Login {

        @ApiModelProperty(value = "사용자 이메일", example = "heejeong@test.com", required = true)
        @NotEmpty(message = "이메일을 입력해주세요.")
        private String email;

        @ApiModelProperty(value = "비밀번호", example = "testpw12", required = true)
        @NotEmpty(message = "비밀번호를 입력해주세요.")
        private String password;

    }

}
