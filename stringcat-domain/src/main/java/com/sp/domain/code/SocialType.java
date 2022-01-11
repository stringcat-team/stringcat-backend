package com.sp.domain.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SocialType {

    GOOGLE("GOOGLE", "구글", true),
    KAKAO("KAKAO", "카카오", true),
    GITHUB("GITHUB", "깃허브", true);

    private final String socialId;
    private final String desc;
    private final boolean active;

}
