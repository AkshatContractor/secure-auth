package com.secure_auth.authdemo.repositories;

import com.secure_auth.authdemo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User getUserByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
