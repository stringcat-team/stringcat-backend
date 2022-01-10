package com.sp.domain.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail (String email);
    Optional<Users> findBySocialId (String socialId);
    Optional<Users> findById (Long id);
    Boolean existsByEmail (String email);

}
