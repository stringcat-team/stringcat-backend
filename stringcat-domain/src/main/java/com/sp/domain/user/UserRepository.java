package com.sp.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById (Long id);
    Optional<User> findByEmail (String email);
    Optional<User> findByEmailAndDeletedFalse (String email);

}
