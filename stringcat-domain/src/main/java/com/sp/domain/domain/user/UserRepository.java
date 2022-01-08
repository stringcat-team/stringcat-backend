package com.sp.domain.domain.user;

import com.sp.domain.domain.ParentRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Optional;

@Repository
public interface UserRepository extends ParentRepository<Users, Long> {

    Optional<Users> findByEmail (String email);
    Optional<Users> findById (Long id);

}
