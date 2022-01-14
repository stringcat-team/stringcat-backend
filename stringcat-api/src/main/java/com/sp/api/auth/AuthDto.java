package com.sp.api.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class AuthDto {

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

    @Data
    @NoArgsConstructor
    @ApiModel("Social Login Param")
    public static class Social {
        private String accessToken;
    }

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @ApiModel("Auth-TokenRefreshForm")
    public static class TokenRefreshForm {
        @ApiModelProperty(value = "user id", notes = "", example = "1234", required = true)
        @NotNull(message = "회원 ID가 설정되지 않았습니다. 다시 로그인해 주세요.")
        private Long userId;
    }
}
