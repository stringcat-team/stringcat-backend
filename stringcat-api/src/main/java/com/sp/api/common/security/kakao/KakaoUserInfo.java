package com.sp.api.common.security.kakao;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class KakaoUserInfo {
    public Long id;
    public String email;
    public String nickname;
    public String image;
}

