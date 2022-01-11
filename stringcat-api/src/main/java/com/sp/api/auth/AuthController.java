package com.sp.api.auth;

import com.sp.api.common.dto.ApiResponse;
import com.sp.api.common.exception.ApiException;
import com.sp.api.user.UserService;
import com.sp.domain.user.User;
import com.sp.domain.user.UserRepository;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    int jwtExpiredMinutes = 5256000;
    int refreshJwtExpiredMinutes = 5256000;

    @PostMapping("/log-in")
    @ApiOperation(value = "일반 로그인 API", notes = "이메일과 비밀번호로 로그인 성공시 jwt 응답에 내려감")
    public ApiResponse<JwtResDto> generalLogin(@Valid @RequestBody AuthDto.Login form) {
        log.info("일반 로그인 REQ : ", form.toString());

        User user = userRepository.findByEmailAndDeletedFalse(form.getEmail())
                .orElseThrow(() -> new ApiException("등록되지 않은 이메일입니다."));

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        form.getEmail(), form.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("일반 로그인 RES : OK");
        log.info("TOKEN userId : ", makeAuthResponse(user).getId());

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
