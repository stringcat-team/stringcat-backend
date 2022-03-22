package com.sp.api.auth.controller;

import com.sp.api.auth.dto.AuthReqDto;
import com.sp.api.auth.dto.AuthResDto;
import com.sp.api.auth.dto.UserInfoDto;
import com.sp.api.auth.security.jwt.JwtHeader;
import com.sp.api.auth.security.jwt.JwtToken;
import com.sp.api.auth.security.jwt.JwtTokenProvider;
import com.sp.api.auth.service.AuthService;
import com.sp.api.auth.service.KakaoService;
import com.sp.api.common.dto.ApiResponse;
import com.sp.domain.user.User;
import com.sp.exception.type.ErrorCode;
import com.sp.exception.type.StringcatCustomException;
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

    @ApiOperation(value = "카카오 accessToken 받기", notes = "테스트용입니다. 승인코드를 넣고 accessToken 받는 로직")
    @GetMapping ("/get/kakao")
    public ApiResponse<String> getKakaoAccessToken(@RequestParam String authorizedCode) {
        log.info("카카오 승인코드 REQ :: {}", authorizedCode);

        String accessToken = kakaoService.getKakaoAccessToken(authorizedCode);

        log.info("카카오 access token :: {}", accessToken);

        return ApiResponse.success(accessToken);
    }

    @ApiOperation(value = "구글 accessToken 받기", notes = "테스트용입니다. 승인코드를 넣고 accessToken 받는 로직")
    @GetMapping ("/get/google")
    public ApiResponse<String> getGoogleAccessToken(@RequestParam String authorizedCode) {
        log.info("카카오 승인코드 REQ :: {}", authorizedCode);

        String accessToken = kakaoService.getKakaoAccessToken(authorizedCode);

        log.info("카카오 access token :: {}", accessToken);

        return ApiResponse.success(accessToken);
}

    @ApiOperation(value = "카카오 사용자 정보받기")
    @PostMapping("/get/user-info")
    public ApiResponse<UserInfoDto.KakaoUserInfo> fetchInfo(String accessToken) {
        log.info("카카오 토큰 REQ :: {}", accessToken);

        UserInfoDto.KakaoUserInfo userInfo = kakaoService.createKakaoUser(accessToken);

        log.info("카카오 사용자 정보 RES :: {}", userInfo);

        return ApiResponse.success(userInfo);
    }

    @ApiOperation(value = "일반 로그인 API (완료)", notes = "이메일과 비밀번호로 로그인 성공시 토큰 반환")
    @PostMapping("/login")
    public ApiResponse<AuthResDto.AuthRes> login(@RequestBody AuthReqDto.Login request) {
        log.info("일반 로그인 REQ :: {}", request.toString());

        User user = authService.findByEmailAndDeletedFalse(request.getEmail())
                .orElseThrow(() -> new StringcatCustomException("존재하지 않는 회원", ErrorCode.NOT_FOUND_USER));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new StringcatCustomException("비밀번호를 확인해주세요.", ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        String accessToken = authService.createToken(request);
        log.info("일반 로그인 RES :: {}", accessToken);

        return ApiResponse.success(new AuthResDto.AuthRes(accessToken, Boolean.FALSE));
    }

    @ApiOperation(value = "일반 회원가입 API (완료)", notes = "소셜 로그인이 아닌 일반 회원가입")
    @PostMapping("/sign-up")
    public ApiResponse signUp(@Valid @RequestBody AuthReqDto.SignUp request) {

        log.info("회원가입 REQ :: {}", request.toString());

        authService.register(request);

        return ApiResponse.success(new AuthResDto.AuthRes());
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
