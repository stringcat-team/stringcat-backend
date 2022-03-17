package com.sp.api.auth.dto;

import com.sp.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GoogleUserDto {

    private String sub;
    private String email;

    public User toUser(String accessToken) {
        return new User(sub, email);
    }

}
