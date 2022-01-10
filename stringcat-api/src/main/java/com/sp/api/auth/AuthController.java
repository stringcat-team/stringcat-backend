package com.sp.api.auth;

import com.sp.api.common.utils.ApiResponse;
import com.sp.api.common.utils.JwtUtil;
import com.sp.api.user.UserDetailServiceImpl;
import com.sp.api.user.UserService;
import com.sp.domain.domain.user.Users;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/auth")
@Api(value = "AuthController : 로그인 및 회원가입 관련 Api")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtil tokenProvider;

    @Autowired
    UserService userService;

    @Autowired
    UserDetailServiceImpl userDetailService;

    @GetMapping("/hello")
    @ApiOperation(value = "헬스체크")
    public String hello() {
        return "I'm ok!!!";
    }

    @PostMapping("/oauth2/kakao")
    @ApiOperation(value = "카카오 소셜 로그인")
    public ApiResponse<AuthResDto> loginByKakao(@RequestBody String token, AuthReqDto.AdditionalInfo form) {
        log.info("카카오 로그인 REQ : " + token);

        String nickname = userService.kakaoLogin(token, form);

        log.info("카카오 로그인 RES : " + nickname);

        return ApiResponse.ok(nickname);
    }

    @PostMapping("/oauth2/google")
    @ApiOperation(value = "구글 소셜 로그인")
    public ApiResponse<AuthResDto> loginByGoogle(@RequestBody String token, AuthReqDto.AdditionalInfo form) {
        log.info("구글 로그인 REQ : " + token);

        String nickname = userService.kakaoLogin(token, form);

        log.info("구글 로그인 RES : " + nickname);

        return ApiResponse.ok(nickname);
    }

    @PostMapping("/oauth2/github")
    @ApiOperation(value = "깃헙 소셜 로그인")
    public ApiResponse<AuthResDto> loginByGithub(@RequestBody String token, AuthReqDto.AdditionalInfo form) {
        log.info("깃헙 로그인 REQ : " + token);

        String nickname = userService.kakaoLogin(token, form);

        log.info("깃헙 로그인 RES : " + nickname);

        return ApiResponse.ok(nickname);
    }

    @PostMapping("/login")
    @ApiOperation(value = "일반 로그인")
    public ApiResponse<AuthResDto> login(@RequestBody AuthReqDto.Login form) {
        log.info("일반 로그인 REQ : " + form);

        Users user = userService.login(form);

        log.info("일반 로그인 RES : " + user);

        return ApiResponse.ok();
    }

    @PostMapping("/sign-up")
    @ApiOperation(value = "회원가입")
    public ApiResponse<AuthResDto> signup(@RequestBody AuthReqDto.SignUp form) {
        log.info("회원가입 REQ : " + form);

        Users res = userService.signUp(form);

        log.info("회원가입 RES : " + res);

        return ApiResponse.ok();
    }

}
