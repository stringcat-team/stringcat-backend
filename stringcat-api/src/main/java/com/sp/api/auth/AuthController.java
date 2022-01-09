package com.sp.api.auth;

import com.sp.api.common.exception.ApiException;
import com.sp.api.common.exception.BadRequestException;
import com.sp.api.common.security.JwtAuthenticationResponse;
import com.sp.api.common.security.TokenProvider;
import com.sp.api.common.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.sp.api.common.utils.ApiResponse;
import com.sp.api.user.UserService;
import com.sp.domain.code.AuthProviders;
import com.sp.domain.domain.user.UserRepository;
import com.sp.domain.domain.user.Users;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

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

    @PostMapping("/login")
    public ApiResponse authenticateUser(@Valid @RequestBody AuthReqDto.Login loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createToken(authentication);

        return ApiResponse.ok(new AuthResDto(token));
    }


    @PostMapping("/signup")
    public ApiResponse registerUser(@Valid @RequestBody AuthReqDto.SignUp form) {
        if(userRepository.findByEmail(form.getEmail())) {
            throw new BadRequestException("Email address already in use.");
        }

        // Creating user's account
        Users user = new Users()
                .setEmail(form.getEmail())
                .setNickname(form.getNickname())
                .setPassword(passwordEncoder.encode(form.getPassword()))
                .setIntro(form.getIntro())
                .setBio(form.getBio())
                .setEmailFlag(form.isEmailFlag());

        Users result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(result.getId()).toUri();

        return ApiResponse.ok();
    }

}
