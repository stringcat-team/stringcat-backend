package com.sp.api.auth;

import com.sp.domain.skill.Skill;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.util.List;

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
    @Accessors(chain = true)
    @NoArgsConstructor
    @ApiModel("Normal Signup form")
    public static class SignUp {

        @ApiModelProperty(value = "이메일", example = "test@test.com", required = true)
        @NotEmpty(message = "사용하실 이메일을 입력해주세요.")
        private String email;

        @ApiModelProperty(value = "닉네임", example = "줄고양이", required = true)
        @NotEmpty(message = "사용하실 닉네임을 입력해주세요.")
        private String nickname;

        @ApiModelProperty(value = "비밀번호", example = "stringcat12", required = true)
        @NotEmpty(message = "비밀번호를 입력해주세요.")
        private String password;

        @ApiModelProperty(value = "비밀번호", example = "stringcat12", required = true)
        @NotEmpty(message = "비밀번호를 확인해주세요..")
        private String password2;

        @ApiModelProperty(value = "기술 스택", example = "")
        private List<Skill> skills;

        @ApiModelProperty(value = "한줄 소개", example = "자신을 한줄로 소개해주세요.")
        private String intro;

        @ApiModelProperty(value = "Github url", example = "stringcat12", required = true)
        @NotEmpty(message = "깃허브 주소를 입력해주세요.")
        private String githubUrl;

    }

    @Data
    @NoArgsConstructor
    @ApiModel("Social Login")
    public static class Social {
        private String accessToken;
    }
}
