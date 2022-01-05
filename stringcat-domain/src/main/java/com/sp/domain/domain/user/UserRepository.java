package com.sp.domain.domain.user;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<SecurityProperties.User, Long> {
}
