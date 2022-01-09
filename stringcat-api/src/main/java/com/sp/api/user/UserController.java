package com.sp.api.user;

import com.sp.api.common.exception.ResourceNotFoundException;
import com.sp.api.common.security.CurrentUser;
import com.sp.api.common.security.oauth2.UserPrincipal;
import com.sp.domain.domain.user.UserRepository;
import com.sp.domain.domain.user.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('ROLE_USER')")
    public Users getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }
}
