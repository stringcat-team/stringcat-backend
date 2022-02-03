package com.sp.domain.email;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailRepository extends JpaRepository<Email, Long> {

    Optional<Email> findByIdAndExpiredAtAfterAndExpired(Long id, LocalDateTime now, boolean expired);

}
