package com.sp.api.user;

import com.sp.api.auth.AuthReqDto;
import com.sp.domain.domain.user.UserRepository;
import com.sp.domain.domain.user.Users;
import com.sp.domain.domain.userskill.UserSkill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


}
