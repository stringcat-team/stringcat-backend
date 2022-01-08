package com.sp.api.auth;

import com.sp.api.ParentController;
import com.sp.api.common.exception.ApiException;
import com.sp.api.common.security.JwtAuthenticationResponse;
import com.sp.api.common.security.JwtProvider;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "/api/auth")
@Api(value = "AuthController : 로그인 및 회원가입 관련 Api")
public class AuthController extends ParentController {

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    UserRepository userRepository;

    @Value("5256000")
    int jwtExpiredMinutes;

    @Value("5256000")
    int refreshJwtExpiredMinutes;


    @ApiOperation(value = "헬스체크")
    @GetMapping("/hello")
    public String hello() {
        return "G O O D";
    }

    @ApiOperation(value = "회원가입")
    @PostMapping(value = "/sign-up", produces = APPLICATION_JSON)
    public ApiResponse register(@Valid @RequestBody AuthDto.SignUp form) {
        log.info("USER 회원가입 REQ :: {}", form.toString());

        userService.signUp(form);

        return ApiResponse.ok();
    }

    @ApiOperation(value = "일반 로그인", notes = "이메일과 비밀번호로 로그인 성공시 jwt 리턴")
    @PostMapping(value = "/login", produces = APPLICATION_JSON)
    public ApiResponse<JwtAuthenticationResponse> login(@Valid @RequestBody AuthDto.Login form) {

        log.info("USER 로그인 REQ :: {}", form.toString());

        Users user = userRepository.findByEmail(form.getEmail())
                .orElseThrow(() -> new ApiException("해당 이메일과 일치하는 정보가 없습니다."));

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        form.getEmail(), form.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("USER 로그인 REQ :: OK");

        log.info("TOKEN userId :: {}", makeAuthResponse(user).getId());

        return ApiResponse.ok(makeAuthResponse(user));

    }

    private JwtAuthenticationResponse makeAuthResponse(Users user) {

        JwtProvider.TokenFormat token = new JwtProvider.TokenFormat();

        token.setId(user.getId())
                .setEmail(user.getEmail());

        String accessToken = jwtProvider.generateToken(token, jwtExpiredMinutes);

        String refreshToken = jwtProvider.generateToken(token, refreshJwtExpiredMinutes);

        return new JwtAuthenticationResponse()
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken)
                .setId(user.getId())
                .setEmail(user.getEmail());
    }


}
