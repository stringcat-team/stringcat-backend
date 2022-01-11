package com.sp.domain.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmailType {

    PASSWORD_SENDER("TO_FIND_PASSWORD", "비밀번호 찾기", true),
    VERIFIER("TO_VERIFY", "이메일 인증 용도", true);

    private final String name;
    private final String desc;
    private final boolean active;

}
