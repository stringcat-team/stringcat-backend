package com.sp.api.auth.dto;

import com.sp.domain.user.User;

public interface ClientProxy {

    User getUserData(String accessToken);

}
