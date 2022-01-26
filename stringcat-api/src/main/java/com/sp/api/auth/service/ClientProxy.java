package com.sp.api.auth.service;

import com.sp.domain.user.User;

public interface ClientProxy {

    User getUserData(String accessToken);

}
