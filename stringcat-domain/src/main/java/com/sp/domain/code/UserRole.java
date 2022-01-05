package com.sp.domain.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {

    GUEST("ROLE_GUEST", "비로그인 사용자"),
    USER("ROLE_USER", "로그인 사용자");

    public final String role;
    public final String desc;

}
