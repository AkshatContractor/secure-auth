package com.secure_auth.authdemo.Repositories;

import com.secure_auth.authdemo.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User getUserByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
