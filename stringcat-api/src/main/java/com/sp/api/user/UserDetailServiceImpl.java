package com.sp.api.user;

import com.sp.api.common.exception.ApiException;
import com.sp.api.common.payload.SpringProfile;
import com.sp.domain.domain.user.UserRepository;
import com.sp.domain.domain.user.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@Profile(SpringProfile.PROD)
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        Optional<Users> user = userRepository.findByEmail(email);

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
