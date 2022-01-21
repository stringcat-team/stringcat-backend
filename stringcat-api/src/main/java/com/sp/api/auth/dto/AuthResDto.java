package com.sp.api.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class AuthResDto {
    private String accessToken;
    private boolean isNewMember;
}
