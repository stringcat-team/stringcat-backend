package com.sp.api.auth;

import com.sp.domain.domain.skill.Skill;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class AuthDto {

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @ApiModel("Auth-UserSignUpForm")
    public static class SignUp {

        @ApiModelProperty(value = "사용자 이메일", example = "stringcat@test.com", required = true)
        @NotEmpty(message = "이메일 주소는 필수 입니다.")
        String email;

        @ApiModelProperty(value = "닉네임", example = "StrCat", required = true)
        @NotEmpty(message = "사용하실 닉네임을 입력해주세요.")
        String nickname;

        @ApiModelProperty(value = "비밀번호", example = "testpw1234", required = true)
        @NotEmpty(message = "비밀번호를 입력해주세요.")
        String password;

        @ApiModelProperty(value = "비밀번호 확인 용도", example = "testpw1234", required = true)
        @NotEmpty(message = "비밀번호를 다시한번 입력해주세요.")
        String password2;

        @ApiModelProperty(value = "비밀번호", example = "Java", notes = "기술스택은 꼭 영어로만 작성되어야 합니다. ")
        List<String> skillList;

        @ApiModelProperty(value = "한줄 소개", example = "안녕하세요. 백엔드 개발자 OOO입니다!")
        String intro;

        @ApiModelProperty(value = "자기 소개", example = "자기소개는 자유롭게 넣을 수 있습니다.")
        String bio;

    }

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @ApiModel("Auth-UserLoginForm")
    public static class Login {

        @ApiModelProperty(value = "사용자 이메일", example = "stringcat@test.com", required = true)
        @NotEmpty(message = "이메일 주소는 필수 입니다.")
        String email;

        @ApiModelProperty(value = "비밀번호", example = "testpw1234", required = true)
        @NotEmpty(message = "비밀번호를 입력해주세요.")
        String password;

    }

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @ApiModel("Auth-VerificationEmail")
    public static class Verification {

        @ApiModelProperty(value = "사용자 이메일", example = "stringcat@test.com", required = true)
        @NotEmpty(message = "이메일 주소는 필수 입니다.")
        String email;

    }

}
