package com.sp.domain.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
public enum PenaltyType {

    DELETE_QUESTION("질문 삭제", true),
    VIOLATION("서비스 위반", true),
    ETC("기타", false);

    @Getter
    private String desc;

    @Getter
    private boolean active;
}
