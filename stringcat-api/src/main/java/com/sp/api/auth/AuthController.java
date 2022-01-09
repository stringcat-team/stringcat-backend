package com.sp.api.auth;

import com.sp.api.common.security.TokenProvider;
import com.sp.api.user.UserService;
import com.sp.domain.domain.user.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/auth")
@Api(value = "AuthController : 로그인 및 회원가입 관련 Api")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    @Value("5256000")
    int jwtExpiredMinutes;

    @Value("5256000")
    int refreshJwtExpiredMinutes;


    @ApiOperation(value = "헬스체크")
    @GetMapping("/hello")
    public String hello() {
        return "G O O D";
    }

}
