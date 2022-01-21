package com.sp.api.grade.dto;

import com.sp.domain.code.GradeRange;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

public class GradeResDto {

    @Data
    @NoArgsConstructor
    @Accessors(chain = true)
    @ApiModel("Entity GradeInfo")
    public static class GradeInfo {

        private Long id;
        private GradeRange name;
        private int minScore;
        private int maxScore;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

    }
}
