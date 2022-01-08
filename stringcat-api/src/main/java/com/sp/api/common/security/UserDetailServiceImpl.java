package com.sp.api.common.security;

import com.sp.api.common.exception.ApiException;
import com.sp.domain.code.UserRole;
import com.sp.domain.domain.user.UserRepository;
import com.sp.domain.domain.user.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername (String email) {
        Optional<Users> user = userRepository.findByEmail(email);

        return processLogin(user);
    }

    public UserDetailImpl loadUserById(Long id) {
        Optional<Users> user = userRepository.findById(id);

        return processLogin(user);

    }

    public UserDetailImpl processLogin(Optional<Users> user) throws UsernameNotFoundException {
        if(!user.isPresent()) {
            throw new ApiException("회원 정보를 찾지 못했습니다.");
        }

        return UserDetailImpl.create(user.get());
    }
}
