package com.sp.api.user;

import com.sp.api.auth.AuthReqDto;
import com.sp.api.auth.AuthResDto;
import com.sp.api.common.exception.ApiException;
import com.sp.api.common.security.kakao.KakaoOAuth2;
import com.sp.api.common.security.kakao.KakaoUserInfo;
import com.sp.api.common.utils.ApiResponse;
import com.sp.api.common.utils.JwtUtil;
import com.sp.domain.code.AuthProviders;
import com.sp.domain.code.UserRole;
import com.sp.domain.domain.user.UserRepository;
import com.sp.domain.domain.user.Users;
import com.sp.domain.domain.userskill.UserSkill;
import com.sp.exception.type.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KakaoOAuth2 kakaoOAuth2;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil tokenProvider;

    public AuthResDto validate(AuthReqDto.JwtRequestForm form) {
        String accessToken = form.getAccessToken();
        String refreshToken = form.getRefreshToken();

        Users user = userRepository.findByEmail(tokenProvider.getUsernameFromToken(accessToken))
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));

        if(!Objects.equals(user.getRefreshToken(), refreshToken)) {
            throw new ApiException(ErrorCode.VALIDATION_EXCEPTION);
        } else {
            String newRefrshToken = tokenProvider.generateRefreshToken();
            userRepository.save(user.refreshToken(newRefrshToken));
            refreshToken = newRefrshToken;
        }

        accessToken = tokenProvider.generateToken(new UserDetailImpl(user));

        return new AuthResDto(accessToken, refreshToken, user);
    }

    public Users signUp(AuthReqDto.SignUp form) {
        String email = form.getEmail();
        Users existUser = userRepository.findByEmail(email).orElse(null);

        if(form.getPassword() != form.getPassword2()) {
            throw new ApiException(ErrorCode.VALIDATION_EXCEPTION);
        }

        if(existUser != null) {
            throw new ApiException(ErrorCode.CONFLICT_EXCEPTION);
        } else {
            String refreshToken = tokenProvider.generateRefreshToken();

            Users newUser = new Users()
                    .setEmail(form.getEmail())
                    .setNickname(form.getNickname())
                    .setPassword(passwordEncoder.encode(form.getPassword()))
                    .setIntro(form.getIntro())
                    .setBio(form.getBio())
                    .setEmailFlag(form.isEmailFlag())
                    .setGithub(form.getGithub())
                    .setCreatedAt(LocalDateTime.now())
                    .setDeleted(false)
                    .setRefreshToken(refreshToken);

            return userRepository.save(newUser);
        }
    }

    public Users login(AuthReqDto.Login form) {
        Users user = userRepository
                .findByEmail(form.getEmail())
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));

        if(passwordEncoder.matches(form.getPassword(), user.getPassword())) {
            return user;
        } else {
            throw new ApiException(ErrorCode.NOT_FOUND);
        }
    }

    public String kakaoLogin(String token, AuthReqDto.AdditionalInfo form) {
        KakaoUserInfo userInfo = kakaoOAuth2.getUserInfo(token);
//        Long kakaoId = userInfo.getId();
        String nickName = userInfo.getNickname();
        String email = userInfo.getEmail();
        String image = userInfo.getImage();

        Users kakaoUser = new Users()
                .setNickname(nickName)
                .setEmail(email)
                .setImagePath(image)
                .setSocialId(AuthProviders.KAKAO)
                .setRole(UserRole.USER)
                .setGithub(form.getGithub())
                .setIntro(form.getIntro())
                .setBio(form.getBio())
                .setCreatedAt(LocalDateTime.now())
                .setDeleted(false);

        userRepository.save(kakaoUser);

        Authentication kakaoNicknameAndEmail = new UsernamePasswordAuthenticationToken(nickName, email);
        Authentication authentication = authenticationManager.authenticate(kakaoNicknameAndEmail);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return nickName;
    }

}
