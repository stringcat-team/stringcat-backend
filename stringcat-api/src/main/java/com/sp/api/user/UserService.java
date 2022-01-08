package com.sp.api.user;

import com.sp.api.auth.AuthDto;
import com.sp.api.common.exception.ApiException;
import com.sp.domain.domain.user.UserRepository;
import com.sp.domain.domain.user.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void signUp(AuthDto.SignUp form) {
        Optional<Users> user = userRepository.findByEmail(form.getEmail());

        if (user.isPresent() || (user.isPresent() && form.getEmail().equals(user.get().getEmail()))) {
            throw new ApiException("이미 사용중인 이메일 입니다.");
        }

        if (!form.getPassword().equals(form.getPassword2())) {
            throw new ApiException("비밀번호가 일치하지 않습니다.");
        }

//        Users newUser = new Users()
//                .setEmail(form.getEmail())
//                .setNickname(form.getNickname())
//                .setGrade(d
//
//
//        newUser = userRepository.save(newUser);
    }

}
