package com.sp.api.auth;

import com.sp.api.common.dto.ApiResponse;
import com.sp.api.common.exception.ApiException;
import com.sp.domain.user.User;
import com.sp.domain.user.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "/guest")
@Api(value = "AuthController - 인증 관련 API")
public class AuthController {

    private static final String APPLICATION_JSON = "application/json; charset=UTF-8";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    int jwtExpiredMinutes = 5256000;
    int refreshJwtExpiredMinutes = 5256000;

    @GetMapping(value = "/hello")
    @Operation(summary = "헬스체크")
    public String hello() {
        return "GOOD";
    }

    @PostMapping(value = "/log-in", produces = APPLICATION_JSON)
    @Operation(summary = "일반 로그인 API", description = "이메일과 비밀번호로 로그인 성공시 jwt 응답에 내려감")
    public ApiResponse<JwtResDto> generalLogin(@RequestBody AuthDto.Login form) {
        log.info("일반 로그인 REQ :: {}", form.toString());

        User user = userRepository.findByEmailAndDeletedFalse(form.getEmail())
                .orElseThrow(() -> new ApiException("등록되지 않은 이메일입니다."));

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        form.getEmail(), form.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("일반 로그인 RES : OK");
        log.info("TOKEN userId :: {} ", makeAuthResponse(user).getId());

        return ApiResponse.success(makeAuthResponse(user));
    }

    @Operation(summary = "토큰 갱신", description = "refresh token을 이용하는 API")
    @PostMapping(value = "/token/refresh", produces = APPLICATION_JSON)
    private ApiResponse<JwtResDto> refreshToken(@Valid @RequestBody AuthDto.TokenRefreshForm form) {

        User user = userRepository.findById(form.getUserId())
                .orElseThrow(() -> new ApiException("회원정보 없음"));

        return ApiResponse.success(makeAuthResponse(user));
    }

    private JwtResDto makeAuthResponse(User user) {

        JwtTokenProvider.TokenFormatter token = new JwtTokenProvider.TokenFormatter();

        token.setId(user.getId())
                .setEmail(user.getEmail());

        String accessToken = tokenProvider.generateToken(token, jwtExpiredMinutes);

        String refreshToken = tokenProvider.generateToken(token, refreshJwtExpiredMinutes);

        return new JwtResDto()
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken)
                .setId(user.getId())
                .setNickname(user.getNickname())
                .setEmail(user.getEmail());
    }
}
