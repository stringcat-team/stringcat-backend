package com.sp.domain.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PenaltyType {

    DELETION("DELETED_QUESTION", "게시글 삭제로 인한 페널티", true),
    VIOLATION("VIOLATED_RULE", "위반", true);

    private final String name;
    private final String desc;
    private final boolean active;

}
