package com.sp.api.user;

import com.sp.api.common.exception.ApiException;
import com.sp.domain.user.User;
import com.sp.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        Optional<User> user = userRepository.findByEmail(email);

        return processLogin(user);
    }

    public CustomUserDetail processLogin(Optional<User> user) throws UsernameNotFoundException {
        if(!user.isPresent()) {
            throw new ApiException("회원정보를 찾지 못했습니다.");
        }

        if(user.get().isDeleted()) {
            throw new ApiException("탈퇴한 회원입니다.");
        }

        return CustomUserDetail.create(user.get());
    }
}
