package com.sp.api.auth.controller;

import com.sp.api.auth.dto.AuthReqDto;
import com.sp.api.auth.dto.AuthResDto;
import com.sp.api.auth.security.jwt.JwtToken;
import com.sp.api.auth.security.jwt.JwtTokenProvider;
import com.sp.api.common.dto.ApiResponse;
import com.sp.api.common.exception.ApiException;
import com.sp.api.user.service.UserService;
import com.sp.domain.user.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
@Api(value = "AuthController - 인증 관련 API")
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider tokenProvider;

    @ApiOperation(value = "일반 로그인 API", notes = "이메일과 비밀번호로 로그인 성공시 토큰 반환")
    @PostMapping("/login")
    public ApiResponse<JwtToken> login(@RequestBody AuthReqDto.Login request) {
        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException("존재하지 않는 Email 입니다."));

        return ApiResponse.success(tokenProvider.generateToken(user.getSocialId(), user.getRole()));
    }

    @ApiOperation(value = "일반 회원가입 API", notes = "소셜 로그인이 아닌 일반 회원가입")
    @PostMapping("/sign-up")
    public ApiResponse signUp(@Valid @RequestBody AuthReqDto.SignUp request) {

        userService.register(request);

        return ApiResponse.success(new AuthResDto());
    }

    @ApiOperation(value = "GOOGLE LOGIN", notes = "구글 엑세스 토큰을 통해 애플리케이션 토큰 반환")
    @PostMapping("/google")
    public ApiResponse<AuthResDto> google(@RequestBody AuthReqDto.Social request) {
        return ApiResponse.success(new AuthResDto());
    }

    @ApiOperation(value = "GITHUB LOGIN", notes = "깃허브 엑세스 토큰을 통해 애플리케이션 토큰 반환")
    @PostMapping("/github")
    public ApiResponse<AuthResDto> github(@RequestBody AuthReqDto.Social request) {
        return ApiResponse.success(new AuthResDto());
    }

    @ApiOperation(value = "KAKAO LOGIN", notes = "카카오 엑세스 토큰을 통해 애플리케이션 토큰 반환")
    @PostMapping("/kakao")
    public ApiResponse<AuthResDto> kakao(@RequestBody AuthReqDto.Social request) {
        return ApiResponse.success(new AuthResDto());
    }

    @ApiOperation(value = "토큰 갱신", notes = "애플리케이션 토큰 갱신")
    @PostMapping("/refresh")
    public ApiResponse<AuthResDto> refreshToken(HttpServletRequest request) {

        return ApiResponse.success(new AuthResDto());
    }

}
