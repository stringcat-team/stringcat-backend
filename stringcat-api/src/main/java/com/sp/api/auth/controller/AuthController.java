package com.sp.api.auth.controller;

import com.sp.api.auth.dto.AuthReqDto;
import com.sp.api.auth.dto.AuthResDto;
import com.sp.api.auth.security.jwt.JwtHeader;
import com.sp.api.auth.security.jwt.JwtToken;
import com.sp.api.auth.security.jwt.JwtTokenProvider;
import com.sp.api.auth.service.AuthService;
import com.sp.api.common.dto.ApiResponse;
import com.sp.api.common.exception.ApiException;
import com.sp.api.user.service.UserService;
import com.sp.domain.user.User;
import com.sp.exception.type.ErrorCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "/auth")
@Api(value = "AuthController - 인증 관련 API")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @ApiOperation(value = "일반 로그인 API", notes = "이메일과 비밀번호로 로그인 성공시 토큰 반환")
    @PostMapping("/login")
    public ApiResponse<JwtToken> login(@RequestBody AuthReqDto.Login request) {

        User user = userService.findByEmailAndDeletedFalse(request.getEmail())
                .orElseThrow(() -> new ApiException("존재하지 않는 Email 입니다."));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ApiException("비밀번호가 일치하지 않습니다. 다시한번 확인해주세요.");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                )
        );

        JwtToken accessToken = tokenProvider.generateToken(user.getSocialId(), user.getRole());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("TOKE :: {}", tokenProvider.generateToken(user.getSocialId(), user.getRole()));

        return ApiResponse.success(accessToken);
    }

    @ApiOperation(value = "일반 회원가입 API", notes = "소셜 로그인이 아닌 일반 회원가입")
    @PostMapping("/sign-up")
    public ApiResponse signUp(@Valid @RequestBody AuthReqDto.SignUp request) {

        userService.register(request);

        return ApiResponse.success(new AuthResDto.AuthRes());
    }

    @ApiOperation(value = "GOOGLE 로그인 API", notes = "구글 엑세스 토큰을 통해 애플리케이션 토큰 반환")
    @PostMapping("/google")
    public ApiResponse<AuthResDto.AuthRes> google(@RequestBody AuthReqDto.Social request) {
        return ApiResponse.success(authService.googleLogin(request));
    }

    @ApiOperation(value = "GITHUB 로그인 API", notes = "깃허브 엑세스 토큰을 통해 애플리케이션 토큰 반환")
    @PostMapping("/github")
    public ApiResponse<AuthResDto.AuthRes> github(@RequestBody AuthReqDto.Social request) {
        return ApiResponse.success(authService.githubLogin(request));
    }

    @ApiOperation(value = "KAKAO 로그인 API", notes = "카카오 엑세스 토큰을 통해 애플리케이션 토큰 반환")
    @PostMapping("/kakao")
    public ApiResponse<AuthResDto.AuthRes> kakao(@RequestBody AuthReqDto.Social request) {

        log.info("kakao REQ 성공 :: {} ", request.toString());
        return ApiResponse.success(authService.kakaoLogin(request));
    }

    @ApiOperation(value = "토큰 갱신", notes = "애플리케이션 토큰 갱신")
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
