package com.sp.api.auth;

import com.sp.api.common.dto.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
@Api(value = "AuthController - 인증 관련 API")
public class AuthController {

    @ApiOperation(value = "일반 로그인 API", notes = "이메일과 비밀번호로 로그인 성공시 토큰 반환")
    @PostMapping("/login")
    public ApiResponse<AuthResDto> login(@RequestBody AuthReqDto.Login request) {

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
