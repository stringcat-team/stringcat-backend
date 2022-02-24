package com.sp.domain.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum UserRole {

    ADMIN("ROLE_ADMIN", "관리자", true),
    GUEST("ROLE_GUEST", "비로그인 사용자", true),
    USER("ROLE_USER", "로그인 사용자", true);

    private final String role;
    private final String desc;
    private final boolean active;

    public static UserRole of(String role) {
        return Arrays.stream(UserRole.values())
                .filter(r -> r.getRole().equals(role))
                .findAny()
                .orElse(GUEST);
    }

}
