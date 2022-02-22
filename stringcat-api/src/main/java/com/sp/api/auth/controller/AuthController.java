package com.sp.api.auth.controller;

import com.sp.api.auth.dto.AuthReqDto;
import com.sp.api.auth.dto.AuthResDto;
import com.sp.api.auth.security.jwt.JwtHeader;
import com.sp.api.auth.security.jwt.JwtToken;
import com.sp.api.auth.security.jwt.JwtTokenProvider;
import com.sp.api.auth.service.AuthService;
import com.sp.api.auth.service.EmailService;
import com.sp.api.auth.service.KakaoService;
import com.sp.api.common.dto.ApiResponse;
import com.sp.exception.type.ErrorCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
@Api(value = "AuthController - 인증 관련 API")
public class AuthController {

    private final AuthService authService;
    private final KakaoService kakaoService;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @ApiOperation(value = "카카오 accessToken 발급받기")
    @PostMapping("/get/access-token")
    public ApiResponse<String> getAccessToken(String code) {
        log.info("카카오 토큰 REQ :: {}", code);

        String token = kakaoService.getAccessToken(code);

        log.info("카카오 토큰 RES :: {}", token);

        return ApiResponse.success(token);
    }

    @ApiOperation(value = "카카오 사용자 정보받기")
    @PostMapping("/get/user-info")
    public ApiResponse<String> fetchInfo(String accessToken) {
        log.info("카카오 토큰 REQ :: {}", accessToken);

        String userInfo = kakaoService.createKakaoUser(accessToken);

        log.info("카카오 토큰 RES :: {}", userInfo);

        return ApiResponse.success(userInfo);
    }

    @ApiOperation(value = "일반 로그인 API (완료)", notes = "이메일과 비밀번호로 로그인 성공시 토큰 반환")
    @PostMapping("/login")
    public ApiResponse<AuthResDto.AuthRes> login(@RequestBody AuthReqDto.Login request) {
        String accessToken = authService.createToken(request);

        return ApiResponse.success(new AuthResDto.AuthRes(accessToken, Boolean.FALSE));
    }

    @ApiOperation(value = "일반 회원가입 API (완료)", notes = "소셜 로그인이 아닌 일반 회원가입")
    @PostMapping("/sign-up")
    public ApiResponse signUp(@Valid @RequestBody AuthReqDto.SignUp request) {

        log.info("회원가입 REQ :: {}", request.toString());

        authService.register(request);

        return ApiResponse.success(new AuthResDto.AuthRes());
    }

    @ApiOperation(value = "GOOGLE 로그인 API (미완료)", notes = "구글 엑세스 토큰을 통해 애플리케이션 토큰 반환")
    @PostMapping("/google")
    public ApiResponse<AuthResDto.AuthRes> google(@RequestBody AuthReqDto.Social request) {
        log.info("Google REQ 성공 :: {} ", request.toString());

        AuthResDto.AuthRes res = authService.googleLogin(request.getAccessToken());

        log.info("Google RES 성공 :: {}", res.toString());

        return ApiResponse.success(res);
    }

    @ApiOperation(value = "GITHUB 로그인 API (미완료)", notes = "깃허브 엑세스 토큰을 통해 애플리케이션 토큰 반환")
    @PostMapping("/github")
    public ApiResponse<AuthResDto.AuthRes> github(@RequestBody AuthReqDto.Social request) {
        log.info("Github REQ 성공 :: {} ", request.toString());

        AuthResDto.AuthRes res = authService.githubLogin(request.getAccessToken());

        log.info("Github RES 성공 :: {} ", res.toString());

        return ApiResponse.success(res);
    }

    @ApiOperation(value = "KAKAO 로그인 API (완료)", notes = "카카오 엑세스 토큰을 통해 애플리케이션 토큰 반환")
    @PostMapping("/kakao")
    public ApiResponse<AuthResDto.AuthRes> kakao(@RequestBody AuthReqDto.Social request) {
        log.info("Kakao REQ 성공 :: {} ", request.toString());

        AuthResDto.AuthRes kakaoRes = authService.kakaoLogin(request.getAccessToken());

        log.info("Kakao RES 성공 :: {}", kakaoRes.toString());

        return ApiResponse.success(kakaoRes);
    }

    @ApiOperation(value = "토큰 갱신 (미완료)", notes = "애플리케이션 토큰 갱신")
    @GetMapping("/refresh")
    public ApiResponse<AuthResDto.AuthRes> refreshToken(HttpServletRequest request) {

        String accessToken = JwtHeader.getAccessToke(request);
        JwtToken jwtToken = tokenProvider.convertStringToJwtToken(accessToken);

        if(!jwtToken.validate()) {
            return ApiResponse.error(ErrorCode.FORBIDDEN_EXCEPTION);
        }

        AuthResDto.AuthRes authRes = authService.updateToken(jwtToken);

        if(authRes == null) {
            return ApiResponse.error(ErrorCode.FORBIDDEN_EXCEPTION);
        }

        return ApiResponse.success(authRes);
    }

}
