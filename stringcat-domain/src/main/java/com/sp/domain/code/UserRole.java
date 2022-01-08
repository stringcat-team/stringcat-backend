package com.sp.domain.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
public enum UserRole {

    GUEST("ROLE_GUEST", "비로그인 사용자", true),
    USER("ROLE_USER", "로그인 사용자", true),
    ADMIN("ROLE_ADMIN", "관리자", true);

    @Getter
    private String name;

    @Getter
    private String desc;

    @Getter
    private boolean active;


}
