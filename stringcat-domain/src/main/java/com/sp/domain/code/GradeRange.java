package com.sp.domain.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GradeRange {

    WELCOME("welcome", "회원가입시 부여되는 등급", true),
    FRIEND("friend", "500~999", true),
    SPECIAL("special", "1000~1999", true),
    HERO("hero", "2000~3999", true),
    PLATINUM("platinum", "4000~7999", true),
    LEGEND("legend", "8000~", true),
    SIGNATURE("VIOLATED_RULE", "위반", true);

    private final String name;
    private final String desc;
    private final boolean active;

}
