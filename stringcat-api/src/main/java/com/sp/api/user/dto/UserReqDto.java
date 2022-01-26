package com.sp.api.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

public class UserReqDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    @ApiModel("Update UserInfo Form")
    public static class UserInfo {

        @ApiModelProperty(value = "깃허브 주소", example = "https://github.com/heejeong-choi")
        private String github;

        @ApiModelProperty(value = "한줄 소개", example = "한줄소개 수정")
        private String bio;

        @ApiModelProperty(value = "자기소개", example = "자유롭게 작성할 수 있습니다.")
        private String intro;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    @ApiModel("User Search")
    public static class Search {

        @ApiModelProperty(value = "사용자 닉네임", example = "stringcat")
        private String nickname;

        @ApiModelProperty(value = "기술명", example = "java")
        private String skill;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    @ApiModel("Password Check")
    public static class PasswordCheck {

        @ApiModelProperty(value = "비밀번호", example = "stringcat12", required = true)
        @NotEmpty(message = "비밀번호를 입력해야합니다.")
        private String password;

    }

}
