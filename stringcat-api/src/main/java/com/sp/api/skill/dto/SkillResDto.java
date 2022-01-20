package com.sp.api.skill.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

public class SkillResDto {

    @Data
    @NoArgsConstructor
    @Accessors(chain = true)
    @ApiModel("Skill명 전체조회")
    public static class Fetch {
        private Long id;
        private String name;
    }
}
