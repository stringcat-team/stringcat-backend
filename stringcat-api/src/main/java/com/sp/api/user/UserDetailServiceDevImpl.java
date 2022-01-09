package com.sp.api.user;

import com.sp.api.common.exception.ApiException;
import com.sp.api.common.payload.SpringProfile;
import com.sp.domain.code.UserRole;
import com.sp.domain.domain.user.UserRepository;
import com.sp.domain.domain.user.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@Profile({SpringProfile.LOCAL, SpringProfile.LOCAL_DEV, SpringProfile.DEV})
public class UserDetailServiceDevImpl implements UserDetailsService {

    private static String USER_ID = "user_id";
    private static String GUEST_ID= "guest_id";

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) {
        if (email == null) throw new ApiException("사용자 email 값이 null 입니다.");

        //테스트를 위한 유저를 강제로 생성한다. 로그인/권한등 목적
        Optional<Users> user;
        if (email.equals(USER_ID)) {
            user = Optional.of(new Users()
                    .setEmail(email)
                    .setRole(UserRole.USER)
                    .setPassword(passwordEncoder.encode("test1234"))
                    .setNickname("최희정")
                    .setGithub("github.com/heejeong-choi")
                    .setEmail("heejeong@test.com")
                    .setBio("안녕하세요")
                    .setEmailFlag(true)
                    .setCreatedAt(LocalDateTime.now())
                    .setDeleted(false));
        } else {
            user = userRepository.findByEmail(email);
        }

        return processLogin(user);
    }

    public UserDetailImpl loadUserById(Long id) {
        Optional<Users> user = userRepository.findById(id);

        return processLogin(user);
    }

    public UserDetailImpl processLogin(Optional<Users> user) throws UsernameNotFoundException {
        if (!user.isPresent()) {
            throw new ApiException("회원정보를 찾지 못했습니다. 입력하신 정보를 확인해 주세요.");
        }

        if (user.get().isDeleted()) {
            throw new ApiException("탈퇴한 회원 입니다.\n 재가입을 원하시는 경우 문의하기를 통해 요청해주세요.");
        }

        return UserDetailImpl.create(user.get());
    }
}
