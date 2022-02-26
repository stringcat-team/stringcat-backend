package com.sp.api.user.service;

import com.sp.api.common.utils.UserDetail;
import com.sp.domain.user.User;
import com.sp.domain.user.UserRepository;
import com.sp.exception.type.ErrorCode;
import com.sp.exception.type.StringcatCustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("load user by username :: {}", username);

        User user = userRepository.findByEmail(username);
        UserDetail userDetail;
        //or null

        if(user != null) {
            userDetail = new UserDetail();
            userDetail.setUser(user);
        } else {
            throw new StringcatCustomException("사용자를 찾을 수 없습니다.", ErrorCode.UNAUTHORIZED_MEMBER);
        }

        return userDetail;
    }
}
