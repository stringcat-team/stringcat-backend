package com.sp.api.skill.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

public class SkillReqDto {

    @Data
    @NoArgsConstructor
    @Accessors(chain = true)
    @ApiModel("Admin Skill Registry")
    public static class Register {

        @ApiModelProperty(value = "skill 명", example = "java", required = true)
        @NotEmpty(message = "skill은 필수값입니다.")
        private String name;

    }
}
