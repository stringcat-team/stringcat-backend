package com.sp.api.auth;

import com.sp.api.common.exception.BadRequestException;
import com.sp.api.common.security.JwtTokenProvider;
import com.sp.api.common.utils.ApiResponse;
import com.sp.api.user.UserService;
import com.sp.domain.domain.user.UserRepository;
import com.sp.domain.domain.user.Users;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/auth")
@Api(value = "AuthController : 로그인 및 회원가입 관련 Api")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    UserService userService;

    @Value("5256000")
    int jwtExpiredMinutes;

    @Value("5256000")
    int refreshJwtExpiredMinutes;

    @GetMapping("/hello")
    @ApiOperation(value = "헬스체크")
    public String hello() {
        return "I'm ok!!!";
    }

}
